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
            System.out.println("1. Exit");
            System.out.print("Select an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline


            switch (choice) {

                case 1:
                    System.out.println("Exiting system...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}