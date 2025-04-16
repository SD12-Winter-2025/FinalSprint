package com.gymmanagement.menu;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.gymmanagement.model.Membership;
import com.gymmanagement.model.User;
import com.gymmanagement.model.WorkoutClass;
import com.gymmanagement.service.MembershipService;
import com.gymmanagement.service.WorkoutClassService;

/**
 * Console interface for member operations.
 */
public class MemberMenu {
    private final Scanner scanner;
    private final MembershipService membershipService;
    private final WorkoutClassService classService;
    private final User currentUser;

    public MemberMenu(Scanner scanner, MembershipService membershipService,
                    WorkoutClassService classService, User currentUser) {
        this.scanner = scanner;
        this.membershipService = membershipService;
        this.classService = classService;
        this.currentUser = currentUser;
    }

    public void show() {
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            try {
                if (!handleChoice(choice)) {
                    break;
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== MEMBER MENU ===");
        System.out.println("1. Browse Classes");
        System.out.println("2. View My Memberships");
        System.out.println("3. Purchase Membership");
        System.out.println("4. Enroll in Class");
        System.out.println("5. Logout");
        System.out.print("Select an option: ");
    }

    private boolean handleChoice(int choice) throws SQLException {
        switch (choice) {
            case 1:
                browseClasses();
                break;
            case 2:
                viewMyMemberships();
                break;
            case 3:
                purchaseMembership();
                break;
            case 4:
                enrollInClass();
                break;
            case 5:
                return false;
            default:
                System.out.println("Invalid option!");
        }
        return true;
    }

    private void browseClasses() throws SQLException {
        List<WorkoutClass> classes = classService.getAllClasses();
        if (classes.isEmpty()) {
            System.out.println("No classes available.");
        } else {
            System.out.println("\n=== AVAILABLE CLASSES ===");
            classes.forEach(System.out::println);
        }
    }

    private void viewMyMemberships() throws SQLException {
        List<Membership> memberships = membershipService.getUserMemberships(currentUser.getId());
        if (memberships.isEmpty()) {
            System.out.println("No active memberships.");
        } else {
            System.out.println("\n=== YOUR MEMBERSHIPS ===");
            memberships.forEach(System.out::println);
        }
    }

    private void purchaseMembership() throws SQLException {
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
            case 1:
                type = "Basic";
                price = 29.99;
                break;
            case 2:
                type = "Premium";
                price = 49.99;
                break;
            case 3:
                type = "Platinum";
                price = 79.99;
                break;
            default:
                System.out.println("Invalid selection.");
                return;
        }

        boolean success = membershipService.purchaseMembership(
            currentUser.getId(),
            type,
            "Standard membership",
            price
        );
        System.out.println(success ? "Purchase successful!" : "Purchase failed.");
    }

    private void enrollInClass() throws SQLException {
        browseClasses();
        System.out.print("Enter class ID to enroll: ");
        int classId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println(classService.enrollMember(currentUser.getId(), classId) 
            ? "Enrollment successful!" 
            : "Enrollment failed.");
    }
}