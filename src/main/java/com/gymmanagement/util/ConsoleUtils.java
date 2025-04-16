package com.gymmanagement.util;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Utility methods for console input validation.
 */
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
                System.out.printf("Input must be between %d and %d\n", min, max);
            } catch (InputMismatchException e) {
                System.out.println("Invalid number");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Field required");
        }
    }
}