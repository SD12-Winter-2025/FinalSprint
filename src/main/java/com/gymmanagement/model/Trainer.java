package com.gymmanagement.model;

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

    // Getters and Setters
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public int getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(int yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }

    @Override
    public String toString() {
        return super.toString() + String.format(
            ", specialization='%s', experience=%d years",
            specialization, yearsOfExperience
        );
    }
}