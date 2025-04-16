package com.gymmanagement.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.gymmanagement.dao.MembershipDAO;
import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.Membership;

/**
 * Handles membership operations including purchases and revenue calculations.
 */
public class MembershipService {
    private final MembershipDAO membershipDAO;
    
    public MembershipService() {
        this.membershipDAO = new MembershipDAO();
    }
    
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
    
    public List<Membership> getUserMemberships(int userId) {
        try {
            return membershipDAO.findByUserId(userId);
        } catch (DatabaseException e) {
            System.err.println("Failed to get memberships: " + e.getMessage());
            return List.of();
        }
    }

    public double calculateTotalRevenue() throws DatabaseException {
        try {
            return membershipDAO.calculateTotalRevenue();
        } catch (SQLException e) {
            throw new DatabaseException("Revenue calculation failed", e);
        }
    }
}