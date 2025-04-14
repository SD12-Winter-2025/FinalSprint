package com.gymmanagement.util;


import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleUtils {
    public static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %d and %d\n", min, max);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    

    public static String readNonEmptyString(Scanner scanner, String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("This field cannot be empty!");
            }
        } while (input.isEmpty());
        return input;
    }
}