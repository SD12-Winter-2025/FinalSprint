package com.gymmanagement.menu;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.gymmanagement.model.User;
import com.gymmanagement.service.MembershipService;
import com.gymmanagement.service.UserService;
import com.gymmanagement.service.WorkoutClassService;

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
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. View All Users");
            System.out.println("2. Delete User");
            System.out.println("3. View Membership Revenue");
            System.out.println("4. View All Classes");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            try {
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
                        return;
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }
    }

    private void viewAllUsers() throws SQLException {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("\n=== ALL USERS ===");
            users.forEach(user -> System.out.println(user));
        }
    }

    private void deleteUser() throws SQLException {
        viewAllUsers();
        System.out.print("\nEnter user ID to delete: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Are you sure? (y/n): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("y")) {
            boolean success = userService.deleteUser(userId);
            System.out.println(success ? "User deleted!" : "Failed to delete user.");
        }
    }

    private void viewRevenue() {
        try {
            double revenue = membershipService.calculateTotalRevenue();
            System.out.printf("\nTotal Revenue: $%.2f\n", revenue);
        } catch (Exception e) {
            System.err.println("Error retrieving total revenue: " + e.getMessage());
        }
    }
    
    private void viewAllClasses() throws SQLException {
        System.out.println("\n=== ALL CLASSES ===");
        classService.getAllClasses().forEach(System.out::println);
    }
}