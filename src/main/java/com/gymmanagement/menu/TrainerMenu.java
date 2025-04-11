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
            System.out.println("3. Update Class");
            System.out.println("4. Delete Class");
            System.out.println("5. Logout");
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
                    updateClass();
                    break;
                case 4:
                    deleteClass();
                    break;
                case 5:
                    return; // Logout
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void updateClass() {
        viewMyClasses();
        System.out.print("Enter class ID to update: ");
        int classId = scanner.nextInt();
        scanner.nextLine();
        WorkoutClass existingClass = classService.getClassById(classId);

        if (existingClass == null) {
            System.out.println("Invalid class ID.");
            return;
        }

        System.out.print("Class Name [" + existingClass.getName() + "]: ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            existingClass.setName(name);
        }

        boolean success = classService.updateClass(existingClass);
        System.out.println(success ? "Class updated successfully!" : "Failed to update class.");
    }

    private void deleteClass() {

        viewMyClasses();
        System.out.print("Enter class ID to delete: ");
        int classId = scanner.nextInt();
        scanner.nextLine();


        boolean success = classService.deleteClass(classId);
        System.out.println(success ? "Class deleted!" : "Failed to delete class.");
    }
}