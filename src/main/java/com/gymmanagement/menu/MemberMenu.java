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
        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║             MEMBER MENU           ║");
        System.out.println("╠═══════════════════════════════════╣");
        System.out.println("║  1. Browse Classes                ║");
        System.out.println("║  2. View My Memberships           ║");
        System.out.println("║  3. Purchase Membership           ║");
        System.out.println("║  4. Enroll in Class               ║");
        System.out.println("║  5. View Enrolled Classes         ║");
        System.out.println("║  6. Logout                        ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println("");
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
                viewEnrolledClasses();
                break;
            case 6:
                System.out.println("Logging out...");
                return false; // Signal to exit the loop
            default:
                System.out.println("Invalid option! Please select a valid menu item.");
        }
        return true; // Continue the loop
    }

    private void browseClasses() throws SQLException {
        List<WorkoutClass> classes = classService.getAllClasses();
        if (classes.isEmpty()) {
            System.out.println("No classes available.");
        } else {
            System.out.println("\n=== AVAILABLE CLASSES ===");
            System.out.println(WorkoutClass.getTableHeader());
            classes.forEach(wc -> System.out.println(wc.toTableRow()));
            System.out.println(WorkoutClass.getTableFooter());
        }
    }
    

    private void viewMyMemberships() throws SQLException {
        List<Membership> memberships = membershipService.getUserMemberships(currentUser.getId());
        if (memberships.isEmpty()) {
            System.out.println("No active memberships.");
        } else {
            System.out.println("\n=== YOUR MEMBERSHIPS ===");
            System.out.println(Membership.getTableHeader());
            memberships.forEach(membership -> System.out.println(membership.toTableRow()));
            System.out.println(Membership.getTableFooter());
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


    private void viewEnrolledClasses() throws SQLException {
        List<WorkoutClass> enrolledClasses = classService.getEnrolledClasses(currentUser.getId()); // Fetch user's enrolled classes
        if (enrolledClasses.isEmpty()) {
            System.out.println("You are not enrolled in any classes.");
        } else {
            System.out.println("\n=== ENROLLED CLASSES ===");
            System.out.println(WorkoutClass.getTableHeader());
            enrolledClasses.forEach(classObj -> System.out.println(classObj.toTableRow()));
            System.out.println(WorkoutClass.getTableFooter());
        }
    }
}

