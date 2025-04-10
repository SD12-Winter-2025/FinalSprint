package com.gymmanagement.util;

import java.util.Scanner;

public class ConsoleUtils {

    public static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            int value = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (value >= min && value <= max) {
                return value;
            }
            System.out.printf("Please enter a number between %d and %d\n", min, max);
        }
    }
}