package com.gymmanagement;

import java.util.Scanner;
public class App {
    private final Scanner scanner;
    private String currentRole;

    public App() {
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }


    public void start() {

        while (true) {

            System.out.println("\n=== Gym Management System ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

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
        currentRole = "ADMIN"; 
        showRoleMenu();
    }

    private void register() {
        System.out.println("\n=== REGISTER NEW USER ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.println("User registered successfully!");
    }

    private void showRoleMenu() {
        switch (currentRole) {
            case "ADMIN":
                System.out.println("Admin Menu");
                break;
            case "TRAINER":
                System.out.println("Trainer Menu");
                break;
            case "MEMBER":
                System.out.println("Member Menu");
                break;
            default:
                System.out.println("Invalid role!");
        }
    }
}