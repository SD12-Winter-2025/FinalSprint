package com.gymmanagement.model;

import java.time.LocalDate;

/**
 * Gym member with additional fitness tracking attributes.
 */
public class Member extends User {
    private LocalDate membershipExpiry;
    private String fitnessGoals;

    public Member() {
        super();
        setRole("MEMBER");
    }

    public Member(String username, String passwordHash, String email) {
        super(username, passwordHash, email, "MEMBER");
    }

    public LocalDate getMembershipExpiry() { 
        return membershipExpiry; 
    }

    public void setMembershipExpiry(LocalDate membershipExpiry) { 
        this.membershipExpiry = membershipExpiry; 
    }
    
    public String getFitnessGoals() { 
        return fitnessGoals; 
    }

    public void setFitnessGoals(String fitnessGoals) { 
        this.fitnessGoals = fitnessGoals; 
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
            ", membershipExpiry=%s, goals='%s'",
            membershipExpiry, fitnessGoals
        );
    }
}