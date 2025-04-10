package com.gymmanagement.menu;

import java.util.List;
import java.util.Scanner;
import com.gymmanagement.model.User;
import com.gymmanagement.service.UserService;

public class AdminMenu {
    private final Scanner scanner;
    private final UserService userService;

    public AdminMenu(Scanner scanner, UserService userService) {
        this.scanner = scanner;
        this.userService = userService;
    }

    public void show() {

        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. View All Users");
            System.out.println("2. Delete User");
            System.out.println("3. Logout");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    deleteUser();
                    break;
                case 3:
                    return; // Logout
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void viewAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("\n=== ALL USERS ===");
            users.forEach(user -> System.out.println(user));
        }
    }

    private void deleteUser() {
        viewAllUsers();
        System.out.print("\nEnter user ID to delete: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        boolean success = userService.deleteUser(userId);
        System.out.println(success ? "User deleted!" : "Failed to delete user.");
    }

}