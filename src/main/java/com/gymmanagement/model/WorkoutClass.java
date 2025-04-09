package com.gymmanagement.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkoutClass {
    private int id;
    private String name;
    private String description;
    private String type;
    private int trainerId;
    private LocalDateTime schedule;
    private int durationMinutes;
    private int maxCapacity;
    private int currentEnrollment;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public int getTrainerId() { return trainerId; }
    public void setTrainerId(int trainerId) { this.trainerId = trainerId; }
    
    public LocalDateTime getSchedule() { return schedule; }
    public void setSchedule(LocalDateTime schedule) { this.schedule = schedule; }
    
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }
    
    public int getCurrentEnrollment() { return currentEnrollment; }
    public void setCurrentEnrollment(int currentEnrollment) { this.currentEnrollment = currentEnrollment; }
    
    // Override toString() method
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format(
            "Class ID: %d\nName: %s\nDescription: %s\nType: %s\nTrainer ID: %d\nSchedule: %s\nDuration: %d minutes\nCapacity: %d/%d\n",
            id, name, description, type, trainerId, 
            (schedule != null ? schedule.format(formatter) : "N/A"),
            durationMinutes, currentEnrollment, maxCapacity
        );
    }
}