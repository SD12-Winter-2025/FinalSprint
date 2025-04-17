package com.gymmanagement.model;

import java.time.LocalDate;

/**
 * Represents a gym member with membership tracking and personal fitness goals.
 * Extends the {@link User} class to include member-specific attributes.
 */
public class Member extends User {
    private LocalDate membershipExpiry;
    private String fitnessGoals;

    /**
     * Default constructor for creating a member with empty values.
     * Sets the default role to "MEMBER".
     */
    public Member() {
        super();
        setRole("MEMBER");
    }

    /**
     * Constructs a member with basic account credentials.
     * 
     * @param username Unique identifier (3-20 characters).
     * @param passwordHash Hashed password for security.
     * @param email Contact email address.
     */
    public Member(String username, String passwordHash, String email) {
        super(username, passwordHash, email, "MEMBER");
    }

    /**
     * Retrieves the expiration date of the membership.
     * 
     * @return Membership expiration date, or {@code null} if not set.
     */
    public LocalDate getMembershipExpiry() {
        return membershipExpiry;
    }

    /**
     * Updates the expiration date of the membership.
     * 
     * @param membershipExpiry New expiration date, or {@code null} to clear the value.
     */
    public void setMembershipExpiry(LocalDate membershipExpiry) {
        this.membershipExpiry = membershipExpiry;
    }

    /**
     * Retrieves the member's fitness goals.
     * 
     * @return Fitness goals description, or {@code null} if not set.
     */
    public String getFitnessGoals() {
        return fitnessGoals;
    }

    /**
     * Updates the member's fitness goals.
     * 
     * @param fitnessGoals New goals description, or {@code null} to clear the value.
     */
    public void setFitnessGoals(String fitnessGoals) {
        this.fitnessGoals = fitnessGoals;
    }

    /**
     * Returns a string representation of the member, including membership expiry and fitness goals.
     * 
     * @return String representation of the member's details.
     */
    @Override
    public String toString() {
        return super.toString() + String.format(
            ", membershipExpiry=%s, goals='%s'",
            membershipExpiry, fitnessGoals
        );
    }
}
