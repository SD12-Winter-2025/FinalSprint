package com.gymmanagement.menu; 


import java.util.Scanner; 


public class MemberMenu { 

    private final Scanner scanner; 


    public MemberMenu(Scanner scanner) { 
        this.scanner = scanner; 
    }   

    public void show() { 
        while (true) { 
            System.out.println("\n=== MEMBER MENU ==="); 
            System.out.println("1. Browse Classes"); 
            System.out.println("2. Logout"); 
            System.out.print("Select an option: "); 

            int choice = scanner.nextInt(); 
            scanner.nextLine(); 

            switch (choice) { 
                case 1: 
                    System.out.println("Browsing classes (functionality to be implemented)."); 
                    break; 
                case 2: 
                    return; // Logout 
                default: 
                    System.out.println("Invalid option!"); 
            } 
        } 
    } 
} 