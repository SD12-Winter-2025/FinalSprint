package com.gymmanagement.model;

/**
 * Gym trainer with specialization and experience tracking.
 */
public class Trainer extends User {
    private String specialization;
    private int yearsOfExperience;

    public Trainer() {
        super();
        setRole("TRAINER");
    }

    public Trainer(String username, String passwordHash, String email) {
        super(username, passwordHash, email, "TRAINER");
    }

    public String getSpecialization() { 
        return specialization; 
    }

    public void setSpecialization(String specialization) { 
        this.specialization = specialization; 
    }
    
    public int getYearsOfExperience() { 
        return yearsOfExperience; 
    }

    public void setYearsOfExperience(int yearsOfExperience) { 
        if (yearsOfExperience < 0) {
            throw new IllegalArgumentException("Experience cannot be negative");
        }
        this.yearsOfExperience = yearsOfExperience; 
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
            ", specialization='%s', experience=%d years",
            specialization, yearsOfExperience
        );
    }
}