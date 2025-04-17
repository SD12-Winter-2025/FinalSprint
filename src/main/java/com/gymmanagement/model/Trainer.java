package com.gymmanagement.model;

/**
 * Represents a certified gym trainer with specialized skills and experience tracking.
 * Extends the {@link User} class to include trainer-specific attributes.
 */
public class Trainer extends User {
    private String specialization;
    private int yearsOfExperience;

    /**
     * Default constructor for creating a trainer with empty values.
     * Sets the default role to "TRAINER".
     */
    public Trainer() {
        super();
        setRole("TRAINER");
    }

    /**
     * Creates a trainer with basic credentials.
     * 
     * @param username Unique identifier (3-20 characters).
     * @param passwordHash Hashed password string for security.
     * @param email Contact email address.
     */
    public Trainer(String username, String passwordHash, String email) {
        super(username, passwordHash, email, "TRAINER");
    }

    /**
     * Retrieves the trainer's primary area of specialization.
     * 
     * @return Specialization (e.g., "Yoga", "Strength Training").
     */
    public String getSpecialization() { 
        return specialization; 
    }

    /**
     * Updates the trainer's specialization area.
     * 
     * @param specialization New training focus area (e.g., "Yoga", "Strength Training").
     */
    public void setSpecialization(String specialization) { 
        this.specialization = specialization; 
    }
    
    /**
     * Retrieves the trainer's years of professional experience.
     * 
     * @return Years of training experience (non-negative).
     */
    public int getYearsOfExperience() { 
        return yearsOfExperience; 
    }

    /**
     * Sets the trainer's years of experience.
     * Ensures that the value is non-negative.
     * 
     * @param yearsOfExperience Positive number representing years of experience.
     * @throws IllegalArgumentException If a negative value is provided.
     */
    public void setYearsOfExperience(int yearsOfExperience) { 
        if (yearsOfExperience < 0) {
            throw new IllegalArgumentException("Experience cannot be negative.");
        }
        this.yearsOfExperience = yearsOfExperience; 
    }

    /**
     * Returns a string representation of the trainer, including specialization and years of experience.
     * 
     * @return Trainer details in string format.
     */
    @Override
    public String toString() {
        return super.toString() + String.format(
            ", specialization='%s', experience=%d years",
            specialization, yearsOfExperience
        );
    }
}
