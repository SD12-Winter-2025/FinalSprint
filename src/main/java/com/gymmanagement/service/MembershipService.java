package com.gymmanagement.service; 


import java.time.LocalDate; 

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
} 