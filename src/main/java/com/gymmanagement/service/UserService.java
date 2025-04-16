package com.gymmanagement.service;

import java.util.List;

import com.gymmanagement.dao.UserDAO;
import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.User;
import com.gymmanagement.util.PasswordHasher;

/**
 * Handles user authentication and management operations.
 */
public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
        initializeDefaultAdmin();
    }

    private void initializeDefaultAdmin() {
        try {
            if (userDAO.findByUsername("admin") == null) {
                User admin = new User("admin", PasswordHasher.hashPassword("adminpassword"), "admin@gym.com", "ADMIN");
                userDAO.create(admin);
            }
        } catch (DatabaseException e) {
            System.err.println("Admin initialization failed: " + e.getMessage());
        }
    }

    public User login(String username, String password) {
        try {
            User user = userDAO.findByUsername(username);
            if (user != null && PasswordHasher.checkPassword(password, user.getPasswordHash())) {
                return user;
            }
        } catch (DatabaseException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return null;
    }

    public boolean register(User user, String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        try {
            if (userDAO.findByUsername(user.getUsername()) != null) {
                return false;
            }
            user.setPasswordHash(PasswordHasher.hashPassword(password));
            return userDAO.create(user);
        } catch (DatabaseException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    public List<User> getAllUsers() {
        try {
            return userDAO.findAll();
        } catch (DatabaseException e) {
            System.err.println("Failed to get users: " + e.getMessage());
            return List.of();
        }
    }

    public boolean deleteUser(int userId) {
        try {
            return userDAO.delete(userId);
        } catch (DatabaseException e) {
            System.err.println("Delete failed: " + e.getMessage());
            return false;
        }
    }
}