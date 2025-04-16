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
import com.gymmanagement.menu.AdminMenu;
import com.gymmanagement.menu.MemberMenu;
import com.gymmanagement.menu.TrainerMenu;
import com.gymmanagement.model.User;
import com.gymmanagement.service.MembershipService;
import com.gymmanagement.service.UserService;
import com.gymmanagement.service.WorkoutClassService;

/**
 * Main application entry point for Gym Management System.
 */
public final class App {
    private final Scanner scanner;
    private final UserService userService;
    private final MembershipService membershipService;
    private final WorkoutClassService classService; // Existing service
    private final WorkoutClassService workoutClassService; // Newly added service
    private User currentUser;

    public App() {
        this.scanner = new Scanner(System.in);
        this.userService = new UserService();
        this.membershipService = new MembershipService();
        this.classService = new WorkoutClassService();
        this.workoutClassService = new WorkoutClassService(); // Initialize new service here
    }

    public static void main(String[] args) {
        new App().run();
    }

    private void run() {
        try (scanner) {
            // Only initialize if needed
            if (!databaseInitialized()) {
                initializeDatabase(
                    "src/main/resources/sql/schema.sql",
                    "src/main/resources/sql/data.sql"
                );
            }
            start(); // This will show your menu
        } catch (IOException | SQLException e) {
            System.err.println("Application error: " + e.getMessage());
        }
    }

    private boolean databaseInitialized() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet tables = meta.getTables(null, null, "users", null)) {
                return tables.next(); // Returns true if users table exists
            }
        }
    }

    private void initializeDatabase(String schemaPath, String dataPath) throws SQLException, IOException {
        try (Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement()) {

            executeSqlFile(stmt, schemaPath, "Database schema initialized");
            executeSqlFile(stmt, dataPath, "Database data loaded");
        }
    }

    private void executeSqlFile(Statement stmt, String filePath, String successMessage) throws IOException, SQLException {
        System.out.println("Loading " + filePath + "...");
        String sql = new String(Files.readAllBytes(Paths.get(filePath)));
        stmt.execute(sql);
        System.out.println(successMessage);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== Gym Management System ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
    
            int choice = -1; // Initialize choice variable
            try {
                if (scanner.hasNextInt()) { // Validate input
                    choice = scanner.nextInt(); // Fetch integer choice
                    scanner.nextLine(); // Consume newline
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear invalid input
                    continue; // Re-prompt user
                }
    
                // Process valid input
                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        register();
                        break;
                    case 3:
                        System.out.println("Goodbye!");
                        return; // Exit the program
                    default:
                        System.out.println("Invalid option! Please select a valid menu item.");
                }
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                scanner.nextLine(); // Clear input buffer
            }
        }
    }
    

    private void login() {
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
            System.out.println("Error: " + e.getMessage()); // Notify user about the password issue
            System.out.println("Returning to main menu...");
        }
    }

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

    private String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private void showRoleMenu() {
        String role = currentUser.getRole();
        if (null == role) {
            System.out.println("Unknown role!");
        } else switch (role) {
            case "ADMIN":
                new AdminMenu(scanner, userService, membershipService, classService).show();
                break;
            case "TRAINER":
                new TrainerMenu(scanner, membershipService, classService, workoutClassService, currentUser).show(); // Updated TrainerMenu instantiation
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
