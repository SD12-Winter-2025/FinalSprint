package com.gymmanagement.menu; 

import java.util.Scanner; 

public class AdminMenu { 

    private final Scanner scanner; 

    public AdminMenu(Scanner scanner) { 
        this.scanner = scanner; 
    }

    public void show() { 
        while (true) { 
            System.out.println("\n=== ADMIN MENU ==="); 
            System.out.println("1. View All Users"); 
            System.out.println("2. Logout"); 
            System.out.print("Select an option: "); 
            int choice = scanner.nextInt(); 
            scanner.nextLine(); 

            switch (choice) { 
                case 1: 
                    System.out.println("Viewing all users (functionality to be added later)."); 
                    break; 
                case 2: 
                    return; // Logout 
                default: 
                    System.out.println("Invalid option!"); 
            } 
        } 
    } 
} 