package com.gymmanagement.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gymmanagement.config.DatabaseConfig;
import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.Membership;

/**
 * Handles database operations for Memberships.
 */
public class MembershipDAO {
    private static final Logger logger = LoggerFactory.getLogger(MembershipDAO.class);

    public boolean create(Membership membership) throws DatabaseException {
        String sql = "INSERT INTO memberships (user_id, type, description, start_date, end_date, price, payment_status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            setMembershipParameters(stmt, membership);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Create failed for user {}", membership.getUserId(), e);
            throw new DatabaseException("Membership creation failed", e);
        }
    }

    public List<Membership> findByUserId(int userId) throws DatabaseException {
        String sql = "SELECT * FROM memberships WHERE user_id = ?";
        List<Membership> memberships = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    memberships.add(mapResultSetToMembership(rs));
                }
            }
            return memberships;
        } catch (SQLException e) {
            logger.error("Lookup failed for user {}", userId, e);
            throw new DatabaseException("Membership lookup failed", e);
        }
    }

    public Map<String, Double> calculateRevenueByType() throws SQLException {
        Map<String, Double> revenueByType = new LinkedHashMap<>();
        String sql = "SELECT type, SUM(price) FROM memberships GROUP BY type";
        
        try (Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                revenueByType.put(rs.getString(1), rs.getDouble(2));
            }
        }
        return revenueByType;
    }

    public Map<String, Integer> countMembershipsByType() throws SQLException {
        Map<String, Integer> counts = new LinkedHashMap<>();
        String sql = "SELECT type, COUNT(*) FROM memberships GROUP BY type";
        
        try (Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                counts.put(rs.getString(1), rs.getInt(2));
            }
        }
        return counts;
    }

    /**
     * @return Total revenue from paid memberships (0.0 if none)
     */
    public double calculateTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(price) AS total FROM memberships WHERE payment_status = 'PAID'";
        
        try (Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            return rs.next() ? rs.getDouble("total") : 0.0;
        }
    }

    private void setMembershipParameters(PreparedStatement stmt, Membership m) throws SQLException {
        stmt.setInt(1, m.getUserId());
        stmt.setString(2, m.getType());
        stmt.setString(3, m.getDescription());
        stmt.setDate(4, Date.valueOf(m.getStartDate()));
        stmt.setDate(5, Date.valueOf(m.getEndDate()));
        stmt.setDouble(6, m.getPrice());
        stmt.setString(7, m.getPaymentStatus());
    }

    private Membership mapResultSetToMembership(ResultSet rs) throws SQLException {
        Membership m = new Membership();
        m.setId(rs.getInt("membership_id"));
        m.setUserId(rs.getInt("user_id"));
        m.setType(rs.getString("type"));
        m.setDescription(rs.getString("description"));
        m.setStartDate(rs.getDate("start_date").toLocalDate());
        m.setEndDate(rs.getDate("end_date").toLocalDate());
        m.setPrice(rs.getDouble("price"));
        m.setPaymentStatus(rs.getString("payment_status"));
        return m;
    }
}