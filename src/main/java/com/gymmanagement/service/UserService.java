package com.gymmanagement.service;

import java.util.List;

import com.gymmanagement.dao.UserDAO;
import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.User;
import com.gymmanagement.util.PasswordHasher;

/**
 * Service class for managing user-related operations.
 * 
 * <p>Provides functionalities for user authentication, registration,
 * and management tasks, including retrieving and deleting users.</p>
 */
public class UserService {
    private final UserDAO userDAO;

    /**
     * Constructs a {@link UserService} instance and initializes the default admin account if it doesn't exist.
     */
    public UserService() {
        this.userDAO = new UserDAO();
        initializeDefaultAdmin();
    }

    /**
     * Initializes a default admin account if none exists in the system.
     * 
     * <p>The admin account is created with the username "admin" and password "adminpassword".</p>
     */
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

    /**
     * Authenticates a user by validating their username and password.
     * 
     * @param username The username provided by the user.
     * @param password The password provided by the user.
     * @return A {@link User} object if authentication is successful, or {@code null} if authentication fails.
     */
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

    /**
     * Registers a new user in the system.
     * 
     * @param user The {@link User} object containing user details (excluding password).
     * @param password The password for the new user (must be at least 8 characters).
     * @return {@code true} if the registration is successful, {@code false} if the username is already taken.
     * @throws IllegalArgumentException If the password is less than 8 characters.
     */
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

    /**
     * Retrieves all users from the system.
     * 
     * @return A {@link List} of {@link User} objects representing all users in the system.
     */
    public List<User> getAllUsers() {
        try {
            return userDAO.findAll();
        } catch (DatabaseException e) {
            System.err.println("Failed to get users: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Deletes a user from the system by their user ID.
     * 
     * @param userId The ID of the user to delete.
     * @return {@code true} if the user was deleted successfully, {@code false} otherwise.
     */
    public boolean deleteUser(int userId) {
        try {
            return userDAO.delete(userId);
        } catch (DatabaseException e) {
            System.err.println("Delete failed: " + e.getMessage());
            return false;
        }
    }
}
