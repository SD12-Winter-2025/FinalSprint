package com.gymmanagement.model;

import java.time.LocalDateTime;

/**
 * Represents a system user with authentication and profile details.
 * Includes shared attributes for ADMIN, TRAINER, and MEMBER roles.
 */
public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String email;
    private String phoneNumber;
    private String address;
    private String role; // ADMIN, TRAINER, MEMBER
    private LocalDateTime createdAt;

    /**
     * Default constructor that initializes a user with the current timestamp.
     */
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructs a user with the specified authentication and profile details.
     * 
     * @param username Unique login identifier (3-20 characters).
     * @param passwordHash Secure hashed password.
     * @param email Valid email address.
     * @param role User role (ADMIN/TRAINER/MEMBER).
     */
    public User(String username, String passwordHash, String email, String role) {
        this();
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
    }

    // --- Core Accessors ---
    
    /**
     * Retrieves the user's unique system identifier.
     * 
     * @return Unique system identifier.
     */
    public int getId() { 
        return id; 
    }
    
    /**
     * Sets the user's unique system identifier.
     * 
     * @param id New unique ID.
     */
    public void setId(int id) { 
        this.id = id; 
    }
    
    /**
     * Retrieves the user's login username.
     * 
     * @return Login username.
     */
    public String getUsername() { 
        return username; 
    }
    
    /**
     * Updates the user's login username.
     * 
     * @param username New login name.
     */
    public void setUsername(String username) { 
        this.username = username; 
    }
    
    /**
     * Retrieves the user's hashed password.
     * 
     * @return Hashed password (never plaintext).
     */
    public String getPasswordHash() { 
        return passwordHash; 
    }
    
    /**
     * Updates the user's hashed password.
     * 
     * @param passwordHash Pre-computed secure hash.
     */
    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash; 
    }
    
    /**
     * Retrieves the user's primary contact email.
     * 
     * @return Contact email.
     */
    public String getEmail() { 
        return email; 
    }
    
    /**
     * Updates the user's primary contact email.
     * 
     * @param email New contact email.
     */
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    /**
     * Retrieves the user's phone number.
     * 
     * @return Phone number.
     */
    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    
    /**
     * Updates the user's phone number.
     * 
     * @param phoneNumber New contact number.
     */
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }
    
    /**
     * Retrieves the user's physical address.
     * 
     * @return Physical address.
     */
    public String getAddress() { 
        return address; 
    }
    
    /**
     * Updates the user's physical address.
     * 
     * @param address New physical address.
     */
    public void setAddress(String address) { 
        this.address = address; 
    }
    
    /**
     * Retrieves the user's system role.
     * 
     * @return Role (ADMIN/TRAINER/MEMBER).
     */
    public String getRole() { 
        return role; 
    }
    
    /**
     * Updates the user's system role.
     * 
     * @param role New role (must be valid).
     */
    public void setRole(String role) { 
        this.role = role; 
    }
    
    /**
     * Retrieves the timestamp of when the user was created.
     * 
     * @return Account creation timestamp.
     */
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    /**
     * Updates the timestamp of when the user was created.
     * 
     * @param createdAt Manual creation time (use with caution).
     */
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    // --- Display Methods ---
    
    /**
     * Provides a formatted table header for user listings.
     * 
     * @return Table header string.
     */
    public static String getTableHeader() {
        return String.format(
            "\n+------+-----------------+----------------------+----------+--------------+----------------------+%n" +
            "| %-4s | %-15s | %-20s | %-8s | %-12s | %-20s |%n" +
            "+------+-----------------+----------------------+----------+--------------+----------------------+",
            "ID", "Username", "Email", "Role", "Phone", "Address");
    }

    /**
     * Provides a formatted table footer for user listings.
     * 
     * @return Table footer string.
     */
    public static String getTableFooter() {
        return "+------+-----------------+----------------------+----------+--------------+----------------------+\n";
    }

    /**
     * Formats the user's details into a table row.
     * 
     * @return Formatted table row string.
     */
    public String toTableRow() {
        String phone = (phoneNumber != null) ? phoneNumber : "N/A";
        String addr = (address != null) ? address : "N/A";
        
        return String.format(
            "| %-4d | %-15s | %-20s | %-8s | %-12s | %-20s |",
            id, username, email, role, phone, addr);
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, username='%s', email='%s', role='%s', phone='%s']",
            id, username, email, role, phoneNumber);
    }
}
