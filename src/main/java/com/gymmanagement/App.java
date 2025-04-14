package com.gymmanagement;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
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

public class App {
    private final Scanner scanner;
    private final UserService userService;
    private final MembershipService membershipService;
    private final WorkoutClassService classService;
    private User currentUser;

    public App() {
        this.scanner = new Scanner(System.in);
        this.userService = new UserService();
        this.membershipService = new MembershipService();
        this.classService = new WorkoutClassService();
    }

    public static void main(String[] args) {
        App app = new App();

        // Initialize the database schema and data
        try {
            app.initializeDatabase(
                "gym-management\\gym-management\\src\\main\\resources\\sql\\schema.sql",
                "gym-management\\gym-management\\src\\main\\resources\\sql\\data.sql"
            );
        } catch (Exception e) { // General catch block for exceptions
            System.err.println("Error during database initialization: " + e.getMessage());
            e.printStackTrace(); // Include stack trace for better debugging
        }

        // Start the application
        app.start();
    }

    private void initializeDatabase(String schemaPath, String dataPath) {
        try (Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement()) {

            // Load schema
            System.out.println("Initializing database schema...");
            String schemaSql = new String(Files.readAllBytes(Paths.get(schemaPath)));
            stmt.execute(schemaSql);
            System.out.println("Database schema initialized successfully!");

            // Load data
            System.out.println("Initializing database data...");
            String dataSql = new String(Files.readAllBytes(Paths.get(dataPath)));
            stmt.execute(dataSql);
            System.out.println("Database data initialized successfully!");

        } catch (SQLException e) {
            System.err.println("SQL Error during database initialization: " + e.getMessage());
            e.printStackTrace(); // Include stack trace for better debugging
        } catch (IOException e) {
            System.err.println("I/O Error during database initialization: " + e.getMessage());
            e.printStackTrace(); // Include stack trace for better debugging
        }
    }
    public void start() {
        while (true) {
            System.out.println("\n=== Gym Management System ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("Exiting system...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option!");
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
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();
        
        System.out.print("Address: ");
        String address = scanner.nextLine();
        
        System.out.println("Select Role:");
        System.out.println("1. Admin");
        System.out.println("2. Trainer");
        System.out.println("3. Member");
        System.out.print("Choice: ");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();
        
        String role;
        switch (roleChoice) {
            case 1:
                role = "ADMIN";
                break;
            case 2:
                role = "TRAINER";
                break;
            case 3:
                role = "MEMBER";
                break;
            default:
                System.out.println("Invalid choice, defaulting to Member");
                role = "MEMBER";
        }
        
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPhoneNumber(phone);
        newUser.setAddress(address);
        newUser.setRole(role);
        
        boolean success = userService.register(newUser, password);
        if (success) {
            System.out.println("Registration successful! You can now login.");
        } else {
            System.out.println("Registration failed. Username or email may already exist.");
        }
    }
    
    private void showRoleMenu() {
        switch (currentUser.getRole()) {
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
                System.out.println("Unknown role detected!");
        }
    }
}