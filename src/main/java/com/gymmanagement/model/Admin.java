package com.gymmanagement.model;

public class Admin extends User {
    public Admin() {
        super();
        setRole("ADMIN");
    }

    public Admin(String username, String passwordHash, String email) {
        super(username, passwordHash, email, "ADMIN");
    }
}