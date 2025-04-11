package com.gymmanagement.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gymmanagement.config.DatabaseConfig;
 import com.gymmanagement.model.Membership;

public class MembershipDAO {

public boolean create(Membership membership) throws SQLException {
    String sql = "INSERT INTO memberships (user_id, type, description, start_date, end_date, price, payment_status) " + 
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, membership.getUserId());
        stmt.setString(2, membership.getType());
        stmt.setString(3, membership.getDescription());
        stmt.setDate(4, Date.valueOf(membership.getStartDate()));
        stmt.setDate(5, Date.valueOf(membership.getEndDate()));
        stmt.setDouble(6, membership.getPrice());
        stmt.setString(7, membership.getPaymentStatus());
    
        return stmt.executeUpdate() > 0;
    }
}

public List<Membership> findByUserId(int userId) throws SQLException {
    String sql = "SELECT * FROM memberships WHERE user_id = ?";
    List<Membership> memberships = new ArrayList<>();
    
    try (Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Membership membership = new Membership();
            membership.setId(rs.getInt("membership_id"));
            membership.setUserId(rs.getInt("user_id"));
            membership.setType(rs.getString("type"));
            membership.setDescription(rs.getString("description"));
            membership.setStartDate(rs.getDate("start_date").toLocalDate());
            membership.setEndDate(rs.getDate("end_date").toLocalDate());
            membership.setPrice(rs.getDouble("price"));
            membership.setPaymentStatus(rs.getString("payment_status"));
            memberships.add(membership);
        }
        return memberships;
    }
}

public double calculateTotalRevenue() throws SQLException {
    String sql = "SELECT SUM(price) AS total FROM memberships WHERE payment_status = 'PAID'";
    
    try (Connection conn = DatabaseConfig.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
        
        return rs.next() ? rs.getDouble("total") : 0.0; // Return 0.0 if no records are found
    }
}

}