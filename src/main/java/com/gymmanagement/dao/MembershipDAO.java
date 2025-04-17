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
 * Data Access Object (DAO) class for performing database operations on memberships.
 * Provides methods for creating, retrieving, and calculating data related to memberships.
 */
public class MembershipDAO {
    private static final Logger logger = LoggerFactory.getLogger(MembershipDAO.class);

    /**
     * Default constructor for creating an instance of the DAO.
     */
    public MembershipDAO() {}

    /**
     * Creates a new membership in the database.
     * 
     * @param membership The {@link Membership} object containing membership details.
     * @return {@code true} if the membership was created successfully, {@code false} otherwise.
     * @throws DatabaseException If a database access error occurs or the operation fails.
     */
    public boolean create(Membership membership) throws DatabaseException {
        String sql = "INSERT INTO memberships (user_id, type, description, start_date, end_date, price, payment_status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            setMembershipParameters(stmt, membership);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Create failed for user {}", membership.getUserId(), e);
            throw new DatabaseException("Membership creation failed.", e);
        }
    }

    /**
     * Retrieves all memberships associated with a specific user ID.
     * 
     * @param userId The ID of the user whose memberships are to be retrieved.
     * @return A {@link List} of {@link Membership} objects for the user.
     * @throws DatabaseException If a database access error occurs or the operation fails.
     */
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
            throw new DatabaseException("Membership lookup failed.", e);
        }
    }

    /**
     * Calculates the total revenue grouped by membership type.
     * 
     * @return A {@link Map} where the key is the membership type and the value is the total revenue.
     * @throws SQLException If a database access error occurs.
     */
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

    /**
     * Counts memberships grouped by membership type.
     * 
     * @return A {@link Map} where the key is the membership type and the value is the count of memberships.
     * @throws SQLException If a database access error occurs.
     */
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
     * Calculates the total revenue from paid memberships.
     * 
     * @return The total revenue as a {@code double}. Returns 0.0 if no paid memberships exist.
     * @throws SQLException If a database access error occurs.
     */
    public double calculateTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(price) AS total FROM memberships WHERE payment_status = 'PAID'";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            return rs.next() ? rs.getDouble("total") : 0.0;
        }
    }

    /**
     * Sets the parameters of a {@link PreparedStatement} with values from a {@link Membership} object.
     * 
     * @param stmt The {@link PreparedStatement} to set parameters for.
     * @param m The {@link Membership} object containing parameter values.
     * @throws SQLException If an error occurs while setting parameters.
     */
    private void setMembershipParameters(PreparedStatement stmt, Membership m) throws SQLException {
        stmt.setInt(1, m.getUserId());
        stmt.setString(2, m.getType());
        stmt.setString(3, m.getDescription());
        stmt.setDate(4, Date.valueOf(m.getStartDate()));
        stmt.setDate(5, Date.valueOf(m.getEndDate()));
        stmt.setDouble(6, m.getPrice());
        stmt.setString(7, m.getPaymentStatus());
    }

    /**
     * Maps the result set of a database query to a {@link Membership} object.
     * 
     * @param rs The {@link ResultSet} from a database query.
     * @return A {@link Membership} object populated with data from the result set.
     * @throws SQLException If an error occurs while reading the result set.
     */
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
