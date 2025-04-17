package com.gymmanagement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.gymmanagement.config.DatabaseConfig;
import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.menu.AdminMenu;
import com.gymmanagement.menu.MemberMenu;
import com.gymmanagement.menu.TrainerMenu;
import com.gymmanagement.model.User;
import com.gymmanagement.service.MembershipService;
import com.gymmanagement.service.UserService;
import com.gymmanagement.service.WorkoutClassService;

/**
 * Main entry point for the Gym Management System application.
 * 
 * <p>Handles initialization, user login and registration, and navigation
 * to role-specific menus based on user type.</p>
 */
public final class App {
    private final Scanner scanner;
    private final UserService userService;
    private final MembershipService membershipService;
    private final WorkoutClassService classService; // Existing service
    private final WorkoutClassService workoutClassService; // Newly added service
    private User currentUser;

    /**
     * Initializes the application and its required services.
     */
    public App() {
        this.scanner = new Scanner(System.in);
        this.userService = new UserService();
        this.membershipService = new MembershipService();
        this.classService = new WorkoutClassService();
        this.workoutClassService = new WorkoutClassService(); // Initialize new service here
    }

    /**
     * Main method to start the application.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new App().run();
    }

    /**
     * Runs the application, handling database initialization and menu navigation.
     */
    private void run() {
        try (scanner) {
            if (!databaseInitialized()) {
                initializeDatabase(
                    "src/main/resources/sql/schema.sql",
                    "src/main/resources/sql/data.sql"
                );
            }
            start();
        } catch (IOException | SQLException e) {
            System.err.println("Application error: " + e.getMessage());
        }
    }

    /**
     * Checks if the database is already initialized by verifying the existence of required tables.
     * 
     * @return {@code true} if the database is initialized, {@code false} otherwise.
     * @throws SQLException If a database access error occurs.
     */
    private boolean databaseInitialized() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet tables = meta.getTables(null, null, "users", null)) {
                return tables.next(); // Returns true if users table exists
            }
        }
    }

    /**
     * Initializes the database by executing SQL scripts for schema creation and data population.
     * 
     * @param schemaPath Path to the SQL schema file.
     * @param dataPath Path to the SQL data file.
     * @throws SQLException If a database error occurs during initialization.
     * @throws IOException If an error occurs while reading the SQL files.
     */
    private void initializeDatabase(String schemaPath, String dataPath) throws SQLException, IOException {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            executeSqlFile(stmt, schemaPath, "Database schema initialized");
            executeSqlFile(stmt, dataPath, "Database data loaded");
        }
    }

    /**
     * Executes the contents of a SQL file.
     * 
     * @param stmt The {@link Statement} object for executing SQL commands.
     * @param filePath Path to the SQL file.
     * @param successMessage Message displayed upon successful execution.
     * @throws IOException If an error occurs while reading the SQL file.
     * @throws SQLException If a database error occurs during execution.
     */
    private void executeSqlFile(Statement stmt, String filePath, String successMessage) throws IOException, SQLException {
        System.out.println("Loading " + filePath + "...");
        String sql = new String(Files.readAllBytes(Paths.get(filePath)));
        stmt.execute(sql);
        System.out.println(successMessage);
    }

    /**
     * Starts the main application menu.
     * Handles user login, registration, or program exit.
     */
    public void start() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════╗");
            System.out.println("║          Gym Management System        ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  1. Login                             ║");
            System.out.println("║  2. Register                          ║");
            System.out.println("║  3. Exit                              ║");
            System.out.println("╚═══════════════════════════════════════╝");
            System.out.println("");
            System.out.print("Select an option: ");

            int choice = -1;
            try {
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                    continue;
                }

                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        register();
                        break;
                    case 3:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option! Please select a valid menu item.");
                }
            } catch (DatabaseException e) {
                System.err.println("An error occurred: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    /**
     * Handles user login and navigates to their role-specific menu.
     */
    private void login() throws DatabaseException {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentUser = userService.login(username, password);

        if (currentUser != null) {
            showRoleMenu();
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    /**
     * Handles user registration and validates input.
     */
    private void register() {
        System.out.println("\n=== REGISTER NEW USER ===");
        User newUser = new User();

        newUser.setUsername(readInput("Username: "));
        String password = readInput("Password: ");
        newUser.setEmail(readInput("Email: "));
        newUser.setPhoneNumber(readInput("Phone Number: "));
        newUser.setAddress(readInput("Address: "));
        newUser.setRole(selectRole());

        try {
            boolean success = userService.register(newUser, password);
            System.out.println(success ? "Registration successful!" : "Registration failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Returning to main menu...");
        }
    }

    /**
     * Prompts the user to select a role during registration.
     * 
     * @return The selected role as a string.
     */
    private String selectRole() {
        System.out.println("Select Role:");
        System.out.println("1. Admin");
        System.out.println("2. Trainer");
        System.out.println("3. Member");
        System.out.print("Choice: ");

        int roleChoice = scanner.nextInt();
        scanner.nextLine();

        switch (roleChoice) {
            case 1:
                return "ADMIN";
            case 2:
                return "TRAINER";
            default:
                return "MEMBER";
        }
    }

    /**
     * Reads input from the console for a given prompt.
     * 
     * @param prompt The message displayed to the user.
     * @return The input entered by the user.
     */
    private String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Displays the menu for the user's specific role.
     */
    private void showRoleMenu() throws DatabaseException {
        String role = currentUser.getRole();
        if (null == role) {
            System.out.println("Unknown role!");
        } else switch (role) {
            case "ADMIN":
                new AdminMenu(scanner, userService, membershipService, classService).show();
                break;
            case "TRAINER":
                new TrainerMenu(scanner, membershipService, classService, currentUser).show();
                break;
            case "MEMBER":
                new MemberMenu(scanner, membershipService, classService, currentUser).show();
                break;
            default:
                System.out.println("Unknown role!");
                break;
        }
    }
}
