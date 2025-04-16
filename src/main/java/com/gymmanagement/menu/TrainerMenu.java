package com.gymmanagement.menu;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import com.gymmanagement.model.User;
import com.gymmanagement.model.WorkoutClass;
import com.gymmanagement.service.MembershipService;
import com.gymmanagement.service.WorkoutClassService;

/**
 * Console interface for trainer operations.
 */
public class TrainerMenu {
    private final Scanner scanner;
    private final MembershipService membershipService;
    private final WorkoutClassService classService;
    private final WorkoutClassService workoutClassService; // Add this field
    private final User currentUser;

    public TrainerMenu(Scanner scanner, MembershipService membershipService,
                    WorkoutClassService classService, WorkoutClassService workoutClassService,
                    User currentUser) {
        this.scanner = scanner;
        this.membershipService = membershipService;
        this.classService = classService;
        this.workoutClassService = workoutClassService; // Initialize this field
        this.currentUser = currentUser;
    }


    public void show() {
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            try {
                if (!handleChoice(choice)) { // Logout scenario
                    break;
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== TRAINER MENU ===");
        System.out.println("1. View My Classes");
        System.out.println("2. Create New Class");
        System.out.println("3. Update Class");
        System.out.println("4. Delete Class");
        System.out.println("5. Purchase Membership");
        System.out.println("6. Logout");
        System.out.print("Select an option: ");
    }

    private boolean handleChoice(int choice) throws SQLException {
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
                return false; // Signal the calling method to exit the menu loop
            default:
                System.out.println("Invalid option! Please select a valid menu item.");
        }
        return true; // Continue the loop
    }

    private void viewMyClasses() throws SQLException {
        // Fetch classes assigned to the current trainer
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

    private void viewAllClasses() throws SQLException {
        // Fetch all classes from the database
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

    private void createClass() throws SQLException {
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
        scanner.nextLine(); // Consume newline
        System.out.print("Max Capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        WorkoutClass newClass = new WorkoutClass();
        newClass.setName(name);
        newClass.setDescription(description);
        newClass.setType(type);
        newClass.setSchedule(schedule);
        newClass.setDurationMinutes(duration);
        newClass.setMaxCapacity(capacity);
    
        // Set the trainerId to the ID of the current user
        newClass.setTrainerId(currentUser.getId());
    
        boolean success = classService.createClass(newClass); // Assuming createClass method exists
        System.out.println(success ? "Class created successfully!" : "Failed to create class.");
    }
    

    private LocalDateTime parseSchedule(String input) {
        try {
            return LocalDateTime.parse(input); 
        } catch (DateTimeParseException e) {
            System.out.println("Invalid format! Please use 'YYYY-MM-DDTHH:MM'. Example: 2025-10-03T04:30");
            return null; 
        }
    }
    

    private void updateClass() throws SQLException {
        // Step 1: Display all available classes
        viewAllClasses(); // Call method to show all classes
    
        // Step 2: Prompt for class ID to update
        System.out.print("\nEnter class ID to update: ");
        if (!scanner.hasNextInt()) { // Validate input
            System.out.println("Invalid input. Returning to menu.");
            scanner.nextLine(); // Clear invalid input
            return;
        }
    
        int classId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        // Step 3: Fetch the class details
        WorkoutClass existingClass = classService.getClassById(classId);
        if (existingClass == null) { // Check if class exists
            System.out.println("Class not found.");
            return;
        }
    
        // Step 4: Prompt for updates
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
    
    
    

    private void updateField(String prompt, String currentValue, java.util.function.Consumer<String> setter) {
        System.out.print(prompt + " [" + currentValue + "]: ");
        String value = scanner.nextLine();
        if (!value.isEmpty()) {
            setter.accept(value);
        }
    }

    private void updateIntField(String prompt, int currentValue, java.util.function.Consumer<Integer> setter) {
        System.out.print(prompt + " [" + currentValue + "]: ");
        String value = scanner.nextLine();
        if (!value.isEmpty()) {
            setter.accept(Integer.valueOf(value));
        }
    }

    private void deleteClass() throws SQLException {
        // Step 1: Display all available classes
        viewAllClasses(); // Call method to show all classes
    
        // Step 2: Prompt for class ID to delete
        System.out.print("\nEnter class ID to delete: ");
        if (!scanner.hasNextInt()) { // Validate input
            System.out.println("Invalid input. Returning to menu.");
            scanner.nextLine(); // Clear invalid input
            return;
        }
    
        int classId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        // Step 3: Fetch the class details
        WorkoutClass existingClass = classService.getClassById(classId);
        if (existingClass == null) { // Check if class exists
            System.out.println("Class not found.");
            return;
        }
    
        // Step 4: Confirm deletion
        System.out.print("Are you sure you want to delete this class? (y/n): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("y")) {
            boolean success = classService.deleteClass(classId);
            System.out.println(success ? "Class deleted successfully!" : "Failed to delete class.");
        } else {
            System.out.println("Class deletion canceled.");
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
                System.out.println("Invalid choice.");
                return;
        }

        System.out.println(membershipService.purchaseMembership(
            currentUser.getId(),
            type,
            "Standard membership",
            price
        ) ? "Membership purchased!" : "Purchase failed.");
    }
}