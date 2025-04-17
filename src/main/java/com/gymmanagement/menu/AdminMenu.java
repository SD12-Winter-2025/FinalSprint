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
 * Console-based menu interface for administrative operations.
 * 
 * <p>This class provides functionalities that allow administrators to:
 * <ul>
 *   <li>View and manage gym users</li>
 *   <li>Monitor membership revenue</li>
 *   <li>Review available workout classes</li>
 *   <li>Logout from the admin interface</li>
 * </ul>
 * 
 * It relies on services like {@link UserService}, {@link MembershipService}, and
 * {@link WorkoutClassService} to perform backend operations. The menu operates through
 * user input from the console.
 */
public class AdminMenu {
    private final Scanner scanner;
    private final UserService userService;
    private final MembershipService membershipService;
    private final WorkoutClassService classService;

    /**
     * Constructor to initialize the admin menu.
     * 
     * @param scanner The {@link Scanner} for reading user input.
     * @param userService The service handling user-related operations.
     * @param membershipService The service handling membership-related operations.
     * @param classService The service handling workout class-related operations.
     */
    public AdminMenu(Scanner scanner, UserService userService,
                     MembershipService membershipService, WorkoutClassService classService) {
        this.scanner = scanner;
        this.userService = userService;
        this.membershipService = membershipService;
        this.classService = classService;
    }

    /**
     * Displays the admin menu and handles user input.
     * The menu will loop until the admin chooses to logout.
     */
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

    /**
     * Prints the admin menu options to the console.
     */
    private void printMenu() {
        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║             ADMIN MENU            ║");
        System.out.println("╠═══════════════════════════════════╣");
        System.out.println("║  1. View All Users                ║");
        System.out.println("║  2. Delete User                   ║");
        System.out.println("║  3. View Membership Revenue       ║");
        System.out.println("║  4. View All Classes              ║");
        System.out.println("║  5. Logout                        ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println("");
        System.out.print("Select an option: ");
    }

    /**
     * Handles the admin's menu selection by executing the relevant option.
     * 
     * @param choice The menu item selected by the admin.
     * @throws SQLException If a database error occurs while processing the menu option.
     */
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

    /**
     * Displays all users in the system in a tabular format.
     * 
     * @throws SQLException If a database error occurs while retrieving the users.
     */
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

    /**
     * Deletes a user from the system based on the user ID.
     * 
     * @throws SQLException If a database error occurs while deleting the user.
     */
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

    /**
     * Displays membership revenue and statistics grouped by type.
     */
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

    /**
     * Displays all workout classes in the system in a tabular format.
     * 
     * @throws SQLException If a database error occurs while retrieving the classes.
     */
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
