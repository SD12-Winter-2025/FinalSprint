package com.gymmanagement.menu;


import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import com.gymmanagement.model.User;
import com.gymmanagement.model.WorkoutClass;
import com.gymmanagement.service.MembershipService;
import com.gymmanagement.service.WorkoutClassService;

public class TrainerMenu {
    private final Scanner scanner;
    private final MembershipService membershipService;
    private final WorkoutClassService classService;
    private final User currentUser;

    public TrainerMenu(Scanner scanner, MembershipService membershipService,
                    WorkoutClassService classService, User currentUser) {
        this.scanner = scanner;
        this.membershipService = membershipService;
        this.classService = classService;
        this.currentUser = currentUser;
    }

    public void show() {
        while (true) {
            System.out.println("\n=== TRAINER MENU ===");
            System.out.println("1. View My Classes");
            System.out.println("2. Create New Class");
            System.out.println("3. Update Class");
            System.out.println("4. Delete Class");
            System.out.println("5. Purchase Membership");
            System.out.println("6. Logout");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
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
                        return;
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }
    }

    private void viewMyClasses() throws SQLException {
        List<WorkoutClass> classes = classService.getClassesByTrainer(currentUser.getId());
        if (classes.isEmpty()) {
            System.out.println("No classes scheduled.");
        } else {
            System.out.println("\n=== YOUR CLASSES ===");
            classes.forEach(System.out::println);
        }
    }

    private void createClass() throws SQLException {
        WorkoutClass newClass = new WorkoutClass();
        newClass.setTrainerId(currentUser.getId());

        System.out.print("Class Name: ");
        newClass.setName(scanner.nextLine());

        System.out.print("Description: ");
        newClass.setDescription(scanner.nextLine());

        System.out.print("Type: ");
        newClass.setType(scanner.nextLine());

        System.out.print("Schedule (YYYY-MM-DDTHH:MM): ");
        newClass.setSchedule(LocalDateTime.parse(scanner.nextLine()));

        System.out.print("Duration (minutes): ");
        newClass.setDurationMinutes(scanner.nextInt());

        System.out.print("Max Capacity: ");
        newClass.setMaxCapacity(scanner.nextInt());
        scanner.nextLine();

        boolean success = classService.createClass(newClass);
        System.out.println(success ? "Class created!" : "Failed to create class.");
    }

    private void updateClass() throws SQLException {
        viewMyClasses();
        System.out.print("\nEnter class ID to update: ");
        int classId = scanner.nextInt();
        scanner.nextLine();

        WorkoutClass existingClass = classService.getClassById(classId);
        if (existingClass == null || existingClass.getTrainerId() != currentUser.getId()) {
            System.out.println("Invalid class ID.");
            return;
        }

        System.out.println("\n=== UPDATE CLASS ===");
        System.out.println("Leave blank to keep current value");

        System.out.print("Class Name [" + existingClass.getName() + "]: ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            existingClass.setName(name);
        }

        System.out.print("Description [" + existingClass.getDescription() + "]: ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) {
            existingClass.setDescription(description);
        }

        System.out.print("Type [" + existingClass.getType() + "]: ");
        String type = scanner.nextLine();
        if (!type.isEmpty()) {
            existingClass.setType(type);
        }

        System.out.print("Schedule [" + existingClass.getSchedule() + "]: ");
        String schedule = scanner.nextLine();
        if (!schedule.isEmpty()) {
            existingClass.setSchedule(LocalDateTime.parse(schedule));
        }

        System.out.print("Duration (minutes) [" + existingClass.getDurationMinutes() + "]: ");
        String duration = scanner.nextLine();
        if (!duration.isEmpty()) {
            existingClass.setDurationMinutes(Integer.parseInt(duration));
        }

        System.out.print("Max Capacity [" + existingClass.getMaxCapacity() + "]: ");
        String capacity = scanner.nextLine();
        if (!capacity.isEmpty()) {
            existingClass.setMaxCapacity(Integer.parseInt(capacity));
        }

        boolean success = classService.updateClass(existingClass);
        System.out.println(success ? "Class updated successfully!" : "Failed to update class.");
    
    }

    private void deleteClass() throws SQLException {
        viewMyClasses();
        System.out.print("\nEnter class ID to delete: ");
        int classId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Confirm deletion (y/n): ");

        if (scanner.nextLine().equalsIgnoreCase("y")) {
            boolean success = classService.deleteClass(classId);
            System.out.println(success ? "Class deleted!" : "Failed to delete class.");
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
            case 1: type = "Basic"; price = 29.99; break;
            case 2: type = "Premium"; price = 49.99; break;
            case 3: type = "Platinum"; price = 79.99; break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        boolean success = membershipService.purchaseMembership(
            currentUser.getId(),
            type,
            "Standard membership", // Added description parameter
            price
        );
        System.out.println(success ? "Membership purchased!" : "Purchase failed.");
    }
}