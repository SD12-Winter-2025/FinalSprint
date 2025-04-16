package com.gymmanagement.menu;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.User;
import com.gymmanagement.service.MembershipService;
import com.gymmanagement.service.UserService;
import com.gymmanagement.service.WorkoutClassService;

/**
 * Console interface for admin operations.
 */
public class AdminMenu {
    private final Scanner scanner;
    private final UserService userService;
    private final MembershipService membershipService;
    private final WorkoutClassService classService;

    public AdminMenu(Scanner scanner, UserService userService,
                   MembershipService membershipService, WorkoutClassService classService) {
        this.scanner = scanner;
        this.userService = userService;
        this.membershipService = membershipService;
        this.classService = classService;
    }

    public void show() {
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            try {
                handleChoice(choice);
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== ADMIN MENU ===");
        System.out.println("1. View All Users");
        System.out.println("2. Delete User");
        System.out.println("3. View Membership Revenue");
        System.out.println("4. View All Classes");
        System.out.println("5. Logout");
        System.out.print("Select an option: ");
    }

    private void handleChoice(int choice) throws SQLException {
        switch (choice) {
            case 1:
                viewAllUsers();
                break;
            case 2:
                deleteUser();
                break;
            case 3:
                viewRevenue();
                break;
            case 4:
                viewAllClasses();
                break;
            case 5:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    private void viewAllUsers() throws SQLException {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("\n=== ALL USERS ===");
            users.forEach(System.out::println);
        }
    }

    private void deleteUser() throws SQLException {
        viewAllUsers();
        System.out.print("\nEnter user ID to delete: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Confirm (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            boolean success = userService.deleteUser(userId);
            System.out.println(success ? "User deleted!" : "Failed to delete user.");
        }
    }

    private void viewRevenue() {
        try {
            System.out.printf("\nTotal Revenue: $%.2f\n", 
                membershipService.calculateTotalRevenue());
        } catch (DatabaseException e) {
            System.err.println("Error retrieving revenue: " + e.getMessage());
        }
    }
    
    private void viewAllClasses() throws SQLException {
        System.out.println("\n=== ALL CLASSES ===");
        classService.getAllClasses().forEach(System.out::println);
    }
}