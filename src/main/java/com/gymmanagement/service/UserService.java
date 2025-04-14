package com.gymmanagement.service;


import java.util.List;

import com.gymmanagement.dao.UserDAO;
import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.User;
import com.gymmanagement.util.PasswordHasher;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();

        // Create default admin
        try {
            if (userDAO.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPasswordHash(PasswordHasher.hashPassword("adminpassword"));
                admin.setEmail("admin@gym.com");
                admin.setRole("ADMIN");
                userDAO.create(admin);
            }
        } catch (DatabaseException e) {
            System.err.println("Error initializing default admin: " + e.getMessage());
        }
    }

    public User login(String username, String password) {
        try {
            User user = userDAO.findByUsername(username);
            if (user != null && PasswordHasher.checkPassword(password, user.getPasswordHash())) {
                return user;
            }
        } catch (DatabaseException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
        return null;
    }

    public boolean register(User user, String password) {
        try {
            if (userDAO.findByUsername(user.getUsername()) != null) {
                return false;
            }
            user.setPasswordHash(PasswordHasher.hashPassword(password));
            return userDAO.create(user);
        } catch (DatabaseException e) {
            System.err.println("Error during user registration: " + e.getMessage());
            return false;
        }
    }

    public List<User> getAllUsers() {
        try {
            return userDAO.findAll();
        } catch (DatabaseException e) {
            System.err.println("Error retrieving users: " + e.getMessage());
            return List.of(); // Return an empty list on error
        }
    }

    public boolean deleteUser(int userId) {
        try {
            return userDAO.delete(userId);
        } catch (DatabaseException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
}