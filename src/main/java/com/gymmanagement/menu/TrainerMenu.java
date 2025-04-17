package com.gymmanagement.menu;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.User;
import com.gymmanagement.model.WorkoutClass;
import com.gymmanagement.service.MembershipService;
import com.gymmanagement.service.WorkoutClassService;

/**
 * Console-based menu for trainer operations, including workout class management and memberships.
 * Allows trainers to view, create, update, and delete classes, as well as purchase memberships.
 */
public class TrainerMenu {
    private final Scanner scanner;
    private final MembershipService membershipService;
    private final WorkoutClassService classService;
    private final User currentUser;

    /**
     * Creates an instance of TrainerMenu with required services.
     * 
     * @param scanner The {@link Scanner} for reading user input.
     * @param membershipService Service for managing membership-related operations.
     * @param classService Service for managing workout class-related operations.
     * @param currentUser The logged-in trainer as a {@link User} object.
     */
    public TrainerMenu(Scanner scanner, MembershipService membershipService, WorkoutClassService classService, User currentUser) {
        this.scanner = scanner;
        this.membershipService = membershipService;
        this.classService = classService;
        this.currentUser = currentUser;
    }

    /**
     * Displays the trainer menu and processes user input.
     * @throws DatabaseException 
     */
    public void show() throws DatabaseException {
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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
     * Prints the trainer menu options to the console.
     */
    private void printMenu() {
        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║            TRAINER MENU           ║");
        System.out.println("╠═══════════════════════════════════╣");
        System.out.println("║  1. View My Classes               ║");
        System.out.println("║  2. Create New Class              ║");
        System.out.println("║  3. Update Class                  ║");
        System.out.println("║  4. Delete Class                  ║");
        System.out.println("║  5. Purchase Membership           ║");
        System.out.println("║  6. Logout                        ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.print("Select an option: ");
    }

    /**
     * Handles the user's menu selection.
     * 
     * @param choice The menu item selected by the user.
     * @return {@code true} to continue, {@code false} to exit the menu.
     * @throws SQLException If a database error occurs while handling the choice.
     */
    private boolean handleChoice(int choice) throws SQLException, DatabaseException {
        switch (choice) {
            case 1:
                viewMyClasses();
                break;
            case 2:
                createClass();
                break;
            case 3:
                updateClass();
                break;
            case 4:
                deleteClass();
                break;
            case 5:
                purchaseMembership();
                break;
            case 6:
                System.out.println("Logging out...");
                return false;
            default:
                System.out.println("Invalid option! Please select a valid menu item.");
                break;
        }
        return true;
    }
    

    /**
     * Displays all workout classes assigned to the trainer.
     * 
     * @throws SQLException If a database error occurs while retrieving classes.
     */
    private void viewMyClasses() throws SQLException {
        List<WorkoutClass> classes = classService.getClassesByTrainer(currentUser.getId());

        if (classes.isEmpty()) {
            System.out.println("You don't have any scheduled classes.");
        } else {
            System.out.println("\n=== MY CLASSES ===");
            System.out.println(WorkoutClass.getTableHeader());
            classes.forEach(wc -> System.out.println(wc.toTableRow()));
            System.out.println(WorkoutClass.getTableFooter());
        }
    }

    /**
     * Displays all available workout classes.
     * 
     * @throws SQLException If a database error occurs while retrieving classes.
     */
    private void viewAllClasses() throws SQLException {
        List<WorkoutClass> classes = classService.getAllClasses();

        if (classes.isEmpty()) {
            System.out.println("No classes found.");
        } else {
            System.out.println("\n=== ALL AVAILABLE CLASSES ===");
            System.out.println(WorkoutClass.getTableHeader());
            classes.forEach(wc -> System.out.println(wc.toTableRow()));
            System.out.println(WorkoutClass.getTableFooter());
        }
    }

    /**
     * Creates a new workout class based on trainer input.
     * 
     * @throws SQLException If a database error occurs while creating the class.
     * @throws DatabaseException 
     */
    private void createClass() throws SQLException, DatabaseException {
        System.out.print("Class Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Type: ");
        String type = scanner.nextLine();
        System.out.print("Schedule (YYYY-MM-DDTHH:MM): ");
        String scheduleInput = scanner.nextLine();

        LocalDateTime schedule = parseSchedule(scheduleInput);
        if (schedule == null) {
            System.out.println("Class creation aborted due to invalid schedule format.");
            return;
        }

        System.out.print("Duration (minutes): ");
        int duration = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Max Capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine();

        WorkoutClass newClass = new WorkoutClass();
        newClass.setName(name);
        newClass.setDescription(description);
        newClass.setType(type);
        newClass.setSchedule(schedule);
        newClass.setDurationMinutes(duration);
        newClass.setMaxCapacity(capacity);
        newClass.setTrainerId(currentUser.getId());

        boolean success = classService.createClass(newClass);
        System.out.println(success ? "Class created successfully!" : "Failed to create class.");
    }

    /**
     * Parses a date-time input provided by the user.
     * 
     * @param input The date-time string in the format `YYYY-MM-DDTHH:MM`.
     * @return A {@link LocalDateTime} object if parsing is successful, or {@code null} if the format is invalid.
     */
    private LocalDateTime parseSchedule(String input) {
        try {
            return LocalDateTime.parse(input);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid format! Please use 'YYYY-MM-DDTHH:MM'.");
            return null;
        }
    }

    
        /**
 * Updates the details of an existing workout class.
 * Allows the trainer to modify class properties such as name, description, type, schedule, duration, and capacity.
 * 
 * @throws SQLException If a database error occurs during the update operation.
         * @throws DatabaseException 
 */
private void updateClass() throws SQLException, DatabaseException {
    viewAllClasses();

    System.out.print("\nEnter class ID to update: ");
    if (!scanner.hasNextInt()) {
        System.out.println("Invalid input. Returning to menu.");
        scanner.nextLine();
        return;
    }

    int classId = scanner.nextInt();
    scanner.nextLine();

    WorkoutClass existingClass = classService.getClassById(classId);
    if (existingClass == null) {
        System.out.println("Class not found.");
        return;
    }

    System.out.println("\n=== UPDATE CLASS ===");
    System.out.println("Leave blank to keep current value");

    updateField("Class Name", existingClass.getName(), existingClass::setName);
    updateField("Description", existingClass.getDescription(), existingClass::setDescription);
    updateField("Type", existingClass.getType(), existingClass::setType);

    System.out.print("Schedule [" + existingClass.getSchedule() + "]: ");
    String schedule = scanner.nextLine();
    if (!schedule.isEmpty()) {
        try {
            existingClass.setSchedule(LocalDateTime.parse(schedule));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. No changes made to schedule.");
        }
    }

    updateIntField("Duration (minutes)", existingClass.getDurationMinutes(), existingClass::setDurationMinutes);
    updateIntField("Max Capacity", existingClass.getMaxCapacity(), existingClass::setMaxCapacity);

    boolean success = classService.updateClass(existingClass);
    System.out.println(success ? "Class updated successfully!" : "Failed to update class.");
}

/**
 * Prompts the trainer to update a string field for a workout class.
 * 
 * @param prompt The prompt message for the field.
 * @param currentValue The current value of the field.
 * @param setter The setter method to apply the update.
 */
private void updateField(String prompt, String currentValue, java.util.function.Consumer<String> setter) {
    System.out.print(prompt + " [" + currentValue + "]: ");
    String value = scanner.nextLine();
    if (!value.isEmpty()) {
        setter.accept(value);
    }
}

/**
 * Prompts the trainer to update an integer field for a workout class.
 * 
 * @param prompt The prompt message for the field.
 * @param currentValue The current value of the field.
 * @param setter The setter method to apply the update.
 */
private void updateIntField(String prompt, int currentValue, java.util.function.Consumer<Integer> setter) {
    System.out.print(prompt + " [" + currentValue + "]: ");
    String value = scanner.nextLine();
    if (!value.isEmpty()) {
        setter.accept(Integer.valueOf(value));
    }
}

/**
 * Deletes a workout class.
 * Allows the trainer to select a class and confirm its deletion.
 * 
 * @throws SQLException If a database error occurs during deletion.
 * @throws DatabaseException 
 */
private void deleteClass() throws SQLException, DatabaseException {
    viewAllClasses();

    System.out.print("\nEnter class ID to delete: ");
    if (!scanner.hasNextInt()) {
        System.out.println("Invalid input. Returning to menu.");
        scanner.nextLine();
        return;
    }

    int classId = scanner.nextInt();
    scanner.nextLine();

    WorkoutClass existingClass = classService.getClassById(classId);
    if (existingClass == null) {
        System.out.println("Class not found.");
        return;
    }

    System.out.print("Are you sure you want to delete this class? (y/n): ");
    String confirmation = scanner.nextLine();
    if (confirmation.equalsIgnoreCase("y")) {
        boolean success = classService.deleteClass(classId);
        System.out.println(success ? "Class deleted successfully!" : "Failed to delete class.");
    } else {
        System.out.println("Class deletion canceled.");
    }
}

/**
 * Allows the trainer to purchase a membership.
 * Presents a list of membership types and processes the trainer's selection.
 * 
 * @throws SQLException If a database error occurs during the purchase.
 */
private void purchaseMembership() throws SQLException {
    System.out.println("\n=== MEMBERSHIP TYPES ===");
    System.out.println("1. Basic ($29.99/month)");
    System.out.println("2. Premium ($49.99/month)");
    System.out.println("3. Platinum ($79.99/month)");
    System.out.print("Select type: ");

    int choice = scanner.nextInt();
    scanner.nextLine();

    String type = null;
    double price = 0.0;

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
            System.out.println("Invalid choice.");
            return;
    }

    boolean success = membershipService.purchaseMembership(
        currentUser.getId(),
        type,
        "Standard membership",
        price
    );
    System.out.println(success ? "Membership purchased!" : "Purchase failed.");
}

}
