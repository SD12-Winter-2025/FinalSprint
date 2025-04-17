package com.gymmanagement.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for hashing and verifying passwords using BCrypt.
 */
public final class PasswordHasher {

    private PasswordHasher() {} // Prevent instantiation

    /**
     * Hashes a plaintext password.
     * 
     * @param plainTextPassword The password to hash.
     * @return The hashed password.
     */
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Verifies a plaintext password against a hashed password.
     * 
     * @param plainTextPassword The password to check.
     * @param hashedPassword The hashed password to verify against.
     * @return {@code true} if the password matches, {@code false} otherwise.
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
