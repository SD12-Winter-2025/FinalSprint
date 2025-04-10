package com.gymmanagement;

import java.util.Scanner;

public class App {

    private final Scanner scanner;

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
        System.out.println("Login functionality not yet implemented."); // Placeholder
    }


    private void register() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.println("Registration functionality not yet implemented."); // Placeholder
    }
}