package com.gymmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gymmanagement.config.DatabaseConfig;
import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.User;

/**
 * Data Access Object (DAO) class for performing database operations on users.
 * Provides methods for creating, retrieving, and deleting user records in the database.
 */
public class UserDAO {

    /**
     * Default constructor for creating an instance of the DAO.
     */
    public UserDAO() {}

    /**
     * Retrieves a user from the database by their username.
     * 
     * @param username The username to search for in the database.
     * @return The {@link User} object representing the user if found, or {@code null} if no user exists with the given username.
     * @throws DatabaseException If a database access error occurs or the operation fails.
     */
    public User findByUsername(String username) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToUser(rs) : null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("User lookup failed: " + username, e);
        }
    }

    /**
     * Creates a new user in the database.
     * 
     * @param user The {@link User} object containing user details to insert into the database.
     * @return {@code true} if the user was created successfully, {@code false} otherwise.
     * @throws DatabaseException If a database access error occurs or the operation fails.
     */
    public boolean create(User user) throws DatabaseException {
        String sql = "INSERT INTO users (username, password_hash, email, phone_number, address, role) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            setUserParameters(stmt, user);
            
            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("User creation failed.", e);
        }
    }

    /**
     * Retrieves all users from the database.
     * 
     * @return A {@link List} of {@link User} objects representing all users in the database.
     * @throws DatabaseException If a database access error occurs or the operation fails.
     */
    public List<User> findAll() throws DatabaseException {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve users.", e);
        }
    }
    
    /**
     * Deletes a user from the database by their user ID.
     * 
     * @param userId The ID of the user to delete.
     * @return {@code true} if the user was deleted successfully, {@code false} otherwise.
     * @throws DatabaseException If a database access error occurs or the operation fails.
     */
    public boolean delete(int userId) throws DatabaseException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("User deletion failed.", e);
        }
    }

    /**
     * Sets the parameters of a {@link PreparedStatement} with values from a {@link User} object.
     * 
     * @param stmt The {@link PreparedStatement} to set parameters for.
     * @param user The {@link User} object containing parameter values.
     * @throws SQLException If an error occurs while setting parameters.
     */
    private void setUserParameters(PreparedStatement stmt, User user) throws SQLException {
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPasswordHash());
        stmt.setString(3, user.getEmail());
        stmt.setString(4, user.getPhoneNumber());
        stmt.setString(5, user.getAddress());
        stmt.setString(6, user.getRole());
    }

    /**
     * Maps the result set of a database query to a {@link User} object.
     * 
     * @param rs The {@link ResultSet} from a database query.
     * @return A {@link User} object with data from the result set.
     * @throws SQLException If an error occurs while reading the result set.
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setAddress(rs.getString("address"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
