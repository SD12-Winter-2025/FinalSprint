package com.gymmanagement.util;

import java.util.Scanner;

public class ConsoleUtils {
    public static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextInt();
    }
}