package com.gymmanagement.service;

import com.gymmanagement.dao.UserDAO;
import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.User;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }


    public User login(String username, String password) {
        try {
            User user = userDAO.findByUsername(username);
            if (user != null && user.getPasswordHash().equals(password)) {
                return user;
            }
        } catch (DatabaseException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
        return null;
    }


    public boolean register(User user) {
        try {
            if (userDAO.findByUsername(user.getUsername()) != null) {
                return false; // Username already exists
            }
            return userDAO.create(user);
        } catch (DatabaseException e) {
            System.err.println("Error during user registration: " + e.getMessage());
            return false;
        }
    }
}