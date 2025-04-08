package com.gymmanagement.dao; 

import java.sql.Connection; 
 import java.sql.Date;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;

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
 
public Membership findById(int membershipId) throws SQLException { 
    String sql = "SELECT * FROM memberships WHERE membership_id = ?"; 
     
    try (Connection conn = DatabaseConfig.getConnection(); 
         PreparedStatement stmt = conn.prepareStatement(sql)) { 
         
        stmt.setInt(1, membershipId); 
        ResultSet rs = stmt.executeQuery(); 
         
        if (rs.next()) { 
            Membership membership = new Membership(); 
            membership.setId(rs.getInt("membership_id")); 
            membership.setUserId(rs.getInt("user_id")); 
            membership.setType(rs.getString("type")); 
            membership.setDescription(rs.getString("description")); 
            membership.setStartDate(rs.getDate("start_date").toLocalDate()); 
            membership.setEndDate(rs.getDate("end_date").toLocalDate()); 
            membership.setPrice(rs.getDouble("price")); 
            membership.setPaymentStatus(rs.getString("payment_status")); 
            return membership; 
        } 
        return null; 
    } 
} 
  

} 