package com.gymmanagement.menu;

import java.util.List;
import java.util.Scanner;
import com.gymmanagement.model.Membership;
import com.gymmanagement.service.MembershipService;

public class MemberMenu {
    private final Scanner scanner;
    private final MembershipService membershipService;

    public MemberMenu(Scanner scanner, MembershipService membershipService) {
        this.scanner = scanner;
        this.membershipService = membershipService;
    }


    public void show() {
        while (true) {
            System.out.println("\n=== MEMBER MENU ===");
            System.out.println("1. Browse Classes");
            System.out.println("2. View My Memberships");
            System.out.println("3. Purchase Membership");
            System.out.println("4. Logout");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();


            switch (choice) {
                case 1:
                    System.out.println("Browsing classes (functionality to be implemented).");
                    break;
                case 2:
                    viewMyMemberships();
                    break;
                case 3:
                    purchaseMembership();
                    break;
                case 4:
                    return; // Logout
                default:
                    System.out.println("Invalid option!");
            }
        }
    }


    private void viewMyMemberships() {
        
        List<Membership> memberships = membershipService.getUserMemberships(1); // Replace with dynamic user ID
        if (memberships.isEmpty()) {
            System.out.println("No active memberships.");
        } else {
            System.out.println("\n=== YOUR MEMBERSHIPS ===");
            memberships.forEach(System.out::println);
        }
    }

    private void purchaseMembership() {

        System.out.println("\n=== MEMBERSHIP TYPES ===");
        System.out.println("1. Basic ($29.99/month)");
        System.out.println("2. Premium ($49.99/month)");
        System.out.println("3. Platinum ($79.99/month)");
        System.out.print("Select type: ");

        int choice = scanner.nextInt();

        scanner.nextLine();
        String type;
        double price;
        switch (choice) {
            case 1: type = "Basic"; price = 29.99; break;
            case 2: type = "Premium"; price = 49.99; break;
            case 3: type = "Platinum"; price = 79.99; break;
            default:
                System.out.println("Invalid selection.");
                return;
        }

        boolean success = membershipService.purchaseMembership(1, type, "Standard membership", price); // Replace user ID with dynamic ID
        System.out.println(success ? "Purchase successful!" : "Purchase failed.");
    }
}