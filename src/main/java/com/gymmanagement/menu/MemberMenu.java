package com.gymmanagement.menu;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.Membership;
import com.gymmanagement.model.User;
import com.gymmanagement.model.WorkoutClass;
import com.gymmanagement.service.MembershipService;
import com.gymmanagement.service.WorkoutClassService;

/**
 * Console-based menu interface for member operations.
 * 
 * This class provides functionalities for gym members, allowing them to:
 * <ul>
 *   <li>Browse available workout classes</li>
 *   <li>View their memberships</li>
 *   <li>Purchase new memberships</li>
 *   <li>Enroll in workout classes</li>
 *   <li>View their enrolled workout classes</li>
 *   <li>Logout from the member interface</li>
 * </ul>
 * 
 * It interacts with {@link MembershipService} and {@link WorkoutClassService} to perform
 * backend operations. The menu operates through user input from the console.
 */
public class MemberMenu {
    private final Scanner scanner;
    private final MembershipService membershipService;
    private final WorkoutClassService classService;
    private final User currentUser;

    /**
     * Constructor to initialize the member menu.
     * 
     * @param scanner The {@link Scanner} for reading user input.
     * @param membershipService The service handling membership-related operations.
     * @param classService The service handling workout class-related operations.
     * @param currentUser The {@link User} object representing the logged-in member.
     */
    public MemberMenu(Scanner scanner, MembershipService membershipService,
                      WorkoutClassService classService, User currentUser) {
        this.scanner = scanner;
        this.membershipService = membershipService;
        this.classService = classService;
        this.currentUser = currentUser;
    }

    /**
     * Displays the member menu and handles user input.
     * The menu loops until the member chooses to logout.
     * @throws DatabaseException 
     */
    public void show() throws DatabaseException {
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

    /**
     * Prints the member menu options to the console.
     */
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

    /**
     * Handles the member's menu selection by executing the relevant option.
     * 
     * @param choice The menu item selected by the member.
     * @return {@code false} if the member chooses to logout, {@code true} otherwise.
     * @throws SQLException If a database error occurs while processing the menu option.
     * @throws DatabaseException 
     */
    private boolean handleChoice(int choice) throws SQLException, DatabaseException {
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
                return false; // Exit the loop
            default:
                System.out.println("Invalid option! Please select a valid menu item.");
        }
        return true; // Continue the loop
    }

    /**
     * Displays all available workout classes in a tabular format.
     * 
     * @throws SQLException If a database error occurs while retrieving the classes.
     */
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

    /**
     * Displays the member's active memberships in a tabular format.
     * 
     * @throws SQLException If a database error occurs while retrieving the memberships.
     */
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

    /**
     * Allows the member to purchase a new membership.
     * 
     * @throws SQLException If a database error occurs while processing the purchase.
     */
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

    /**
     * Allows the member to enroll in a workout class.
     * 
     * @throws SQLException If a database error occurs while processing the enrollment.
     */
    private void enrollInClass() throws SQLException, DatabaseException {
        browseClasses();
        System.out.print("Enter class ID to enroll: ");
        int classId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println(classService.enrollMember(currentUser.getId(), classId) 
            ? "Enrollment successful!" 
            : "Enrollment failed.");
    }

    /**
     * Displays the workout classes in which the member is enrolled.
     * 
     * @throws SQLException If a database error occurs while retrieving the classes.
     */
    private void viewEnrolledClasses() throws SQLException, DatabaseException {
        List<WorkoutClass> enrolledClasses = classService.getEnrolledClasses(currentUser.getId());
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
