package com.gymmanagement.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.gymmanagement.dao.MembershipDAO;
import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.Membership;

/**
 * Manages membership transactions including purchases, renewals, and revenue reporting.
 * Handles all business logic between controllers and membership data access.
 */
public class MembershipService {
    private final MembershipDAO membershipDAO;

    /**
     * Initializes service with default DAO implementation.
     */
    public MembershipService() {
        this.membershipDAO = new MembershipDAO();
    }

    /**
     * Processes new membership purchase with 1-month default duration.
     * @param userId Target user account ID
     * @param type Membership tier (e.g., "PREMIUM")
     * @param description Optional benefits description
     * @param price Membership cost
     * @return true if purchase succeeded, false on failure
     */
    public boolean purchaseMembership(int userId, String type, String description, double price) {
        try {
            Membership membership = new Membership();
            membership.setUserId(userId);
            membership.setType(type);
            membership.setDescription(description);
            membership.setPrice(price);
            membership.setStartDate(LocalDate.now());
            membership.setEndDate(LocalDate.now().plusMonths(1));
            membership.setPaymentStatus("PENDING");

            return membershipDAO.create(membership);
        } catch (DatabaseException e) {
            System.err.println("Purchase failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all memberships for a given user.
     * @param userId Target user account ID
     * @return List of memberships (empty list on error)
     */
    public List<Membership> getUserMemberships(int userId) {
        try {
            return membershipDAO.findByUserId(userId);
        } catch (DatabaseException e) {
            System.err.println("Failed to get memberships: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Calculates gross revenue from all memberships.
     * @return Total revenue amount
     * @throws DatabaseException On SQL errors
     */
    public double calculateTotalRevenue() throws DatabaseException {
        try {
            return membershipDAO.calculateTotalRevenue();
        } catch (SQLException e) {
            throw new DatabaseException("Revenue calculation failed", e);
        }
    }

    /**
     * Breaks down revenue by membership type.
     * @return Map of type → revenue (e.g., {"PREMIUM": 500.00})
     * @throws DatabaseException On SQL errors
     */
    public Map<String, Double> getRevenueByMembershipType() throws DatabaseException {
        try {
            return membershipDAO.calculateRevenueByType();
        } catch (SQLException e) {
            throw new DatabaseException("Error calculating revenue by type", e);
        }
    }

    /**
     * Counts active memberships by type.
     * @return Map of type → count (e.g., {"STANDARD": 42})
     * @throws DatabaseException On SQL errors
     */
    public Map<String, Integer> getMembershipCounts() throws DatabaseException {
        try {
            return membershipDAO.countMembershipsByType();
        } catch (SQLException e) {
            throw new DatabaseException("Error counting memberships", e);
        }
    }
}