package com.gymmanagement.menu;

import java.util.Scanner;
import java.util.List;
import com.gymmanagement.model.WorkoutClass;

import com.gymmanagement.service.WorkoutClassService;


public class TrainerMenu {
    private final Scanner scanner;
    private final WorkoutClassService classService;
    public TrainerMenu(Scanner scanner, WorkoutClassService classService) {
        this.scanner = scanner;
        this.classService = classService;
    }

    public void show() {

        while (true) {
            System.out.println("\n=== TRAINER MENU ===");
            System.out.println("1. View My Classes");
            System.out.println("2. Create New Class");
            System.out.println("3. Logout");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewMyClasses();
                    break;
                case 2:
                    createClass();
                    break;
                case 3:
                    return; // Logout
                default:
                    System.out.println("Invalid option!");
            }
        }
    }


    private void viewMyClasses() {
        List<WorkoutClass> classes = classService.getClassesByTrainer(1); // Replace with dynamic trainer ID
        if (classes.isEmpty()) {
            System.out.println("No classes scheduled.");
        } else {
            System.out.println("\n=== YOUR CLASSES ===");
            classes.forEach(System.out::println);
        }
    }

    private void createClass() {
        WorkoutClass newClass = new WorkoutClass();

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
}