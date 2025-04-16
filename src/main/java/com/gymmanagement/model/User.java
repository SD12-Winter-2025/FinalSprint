package com.gymmanagement.model;

import java.time.LocalDateTime;

/**
 * Base entity for all system users with core authentication and profile data.
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

    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(String username, String passwordHash, String email, String role) {
        this();
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
    }

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }


    //Adding table display for User Information
    public static String getTableHeader() {
        return String.format(
            "\n+------+-----------------+----------------------+----------+--------------+----------------------+%n" +
            "| %-4s | %-15s | %-20s | %-8s | %-12s | %-20s |%n" +
            "+------+-----------------+----------------------+----------+--------------+----------------------+",
            "ID", "Username", "Email", "Role", "Phone", "Address"
        );
    }

    public static String getTableFooter() {
        return "+------+-----------------+----------------------+----------+--------------+----------------------+\n";
    }

    public String toTableRow() {
        // Handle null values safely
        String phone = (phoneNumber != null) ? phoneNumber : "N/A";
        String addr = (address != null) ? address : "N/A";
        
        return String.format(
            "| %-4d | %-15s | %-20s | %-8s | %-12s | %-20s |",
            id, 
            username, 
            email, 
            role, 
            phone, 
            addr
        );
    }


    @Override
    public String toString() {
        return String.format(
            "User[id=%d, username='%s', email='%s', role='%s', phone='%s']",
            id, username, email, role, phoneNumber
        );
    }
}