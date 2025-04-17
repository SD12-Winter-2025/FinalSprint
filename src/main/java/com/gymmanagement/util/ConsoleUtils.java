package com.gymmanagement.util;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Provides utility methods for reading and validating user input from the console.
 */
public class ConsoleUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private ConsoleUtils() {}

    /**
     * Reads an integer from the console within the specified range.
     * Prompts the user until a valid value is provided.
     * 
     * @param scanner The {@link Scanner} for reading input.
     * @param prompt The message to display to the user.
     * @param min The minimum allowed value (inclusive).
     * @param max The maximum allowed value (inclusive).
     * @return A valid integer within the specified range.
     */
    public static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Input must be between %d and %d.\n", min, max);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    /**
     * Reads a non-empty string from the console.
     * Prompts the user until valid input is provided.
     * 
     * @param scanner The {@link Scanner} for reading input.
     * @param prompt The message to display to the user.
     * @return A non-empty string entered by the user.
     */
    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("This field cannot be empty. Please try again.");
        }
    }
}
