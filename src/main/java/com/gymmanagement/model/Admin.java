package com.gymmanagement.model;

/**
 * Administrator user with system management privileges.
 * Automatically sets role to "ADMIN" on construction.
 */
public class Admin extends User {
    
    /**
     * Creates an admin with empty credentials.
     */
    public Admin() {
        super();
        setRole("ADMIN");
    }

    /**
     * Creates an admin with specified credentials.
     * @param username  Unique login name (5-20 chars)
     * @param passwordHash  Pre-hashed password string
     * @param email  Valid email address
     */
    public Admin(String username, String passwordHash, String email) {
        super(username, passwordHash, email, "ADMIN");
    }
}