package com.gymmanagement.menu;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.User;
import com.gymmanagement.model.WorkoutClass;
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
                if (choice == 5) { // Logout scenario
                    break;
                }
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
                System.out.println("Logging out...");
                return; // Exit the Admin menu and return to start()
            default:
                System.out.println("Invalid option! Please select a valid menu item.");
        }
    }
    

    //Table display of User Information
    private void viewAllUsers() throws SQLException {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("\nNo users found.");
        } else {
            System.out.println("\n=== ALL USERS ===");
            
            // Print table header
            System.out.println(User.getTableHeader());
            
            // Print each user as a row
            users.forEach(user -> System.out.println(user.toTableRow()));
            
            // Print table footer
            System.out.println(User.getTableFooter());
            
            // Show count
            System.out.println("Total users: " + users.size());
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
            double totalRevenue = membershipService.calculateTotalRevenue();
            Map<String, Double> revenueByType = membershipService.getRevenueByMembershipType();
            Map<String, Integer> membershipCounts = membershipService.getMembershipCounts();
            
            System.out.println("\n+-----------------+--------+-------------+----------+");
            System.out.println("| Membership Type | Count  | Revenue     | % of Total |");
            System.out.println("+-----------------+--------+-------------+----------+");
            
            revenueByType.forEach((type, revenue) -> {
                int count = membershipCounts.getOrDefault(type, 0);
                double percentage = (totalRevenue > 0) ? (revenue / totalRevenue) * 100 : 0;
                System.out.printf("| %-15s | %-6d | $%-10.2f | %-8.1f |%n",
                    type, count, revenue, percentage);
            });
            
            System.out.println("+-----------------+--------+-------------+----------+");
            System.out.printf("| %-15s | %-6s | $%-10.2f | %-8s |%n",
                "TOTAL", "", totalRevenue, "100%");
            System.out.println("+-----------------+--------+-------------+----------+");
            
        } catch (DatabaseException e) {
            System.err.println("Error retrieving revenue data: " + e.getMessage());
        }
    }
    
    private void viewAllClasses() throws SQLException {
    List<WorkoutClass> classes = classService.getAllClasses();
    if (classes.isEmpty()) {
        System.out.println("\nNo classes found.");
    } else {
        System.out.println("\n=== ALL CLASSES ===");
        
        // Print table header
        System.out.println(WorkoutClass.getTableHeader());
        
        // Print each class as a row
        classes.forEach(wc -> System.out.println(wc.toTableRow()));
        
        // Print table footer
        System.out.println(WorkoutClass.getTableFooter());
        
        // Show count
        System.out.println("Total classes: " + classes.size());
    }
}
}