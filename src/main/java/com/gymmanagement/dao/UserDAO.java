package com.gymmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gymmanagement.config.DatabaseConfig;
import com.gymmanagement.model.User;

public class UserDAO {

public boolean create(User user) throws SQLException {
    String sql = "INSERT INTO users (username, password_hash, email, phone_number, address, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPasswordHash());
        stmt.setString(3, user.getEmail());
        stmt.setString(4, user.getPhoneNumber());
        stmt.setString(5, user.getAddress());
        stmt.setString(6, user.getRole());
        
        return stmt.executeUpdate() > 0;
    }
}

public User findByUsername(String username) throws SQLException {
    String sql = "SELECT * FROM users WHERE username = ?";
    try (Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return mapResultSetToUser(rs);
        }
        return null;
    }
}

public List<User> findAll() throws SQLException {
    String sql = "SELECT * FROM users";
    List<User> users = new ArrayList<>();
    
    try (Connection conn = DatabaseConfig.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            users.add(mapResultSetToUser(rs));
        }
    }
    return users;
}

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