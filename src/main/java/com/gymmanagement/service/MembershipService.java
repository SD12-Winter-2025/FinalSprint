package com.gymmanagement.service;

import java.time.LocalDate;
import java.util.List;

import com.gymmanagement.dao.MembershipDAO;
import com.gymmanagement.model.Membership;

public class MembershipService {
    private final MembershipDAO membershipDAO;

    public MembershipService() {
        this.membershipDAO = new MembershipDAO();
    }

    public boolean purchaseMembership(int userId, String type, double price) {
        try { 
            Membership membership = new Membership();
            membership.setUserId(userId);
            membership.setType(type);
            membership.setPrice(price);
            membership.setStartDate(LocalDate.now());
            membership.setEndDate(LocalDate.now().plusMonths(1));
            return membershipDAO.create(membership);
        } catch (Exception e) {
            System.err.println("Error purchasing membership: " + e.getMessage());
            return false;
        }
    }

    public List<Membership> getUserMemberships(int userId) {
        try {
            return membershipDAO.findByUserId(userId);
        } catch (Exception e) {
            System.err.println("Error retrieving memberships for user " + userId + ": " + e.getMessage());
            return List.of();
        }
    }
}