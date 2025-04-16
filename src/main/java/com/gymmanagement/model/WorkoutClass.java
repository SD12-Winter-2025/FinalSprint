package com.gymmanagement.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Scheduled gym class with enrollment tracking and capacity management.
 */
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
    public void setDurationMinutes(int durationMinutes) { 
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        this.durationMinutes = durationMinutes; 
    }
    
    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { 
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.maxCapacity = maxCapacity; 
    }
    
    public int getCurrentEnrollment() { return currentEnrollment; }
    public void setCurrentEnrollment(int currentEnrollment) { 
        if (currentEnrollment < 0 || currentEnrollment > maxCapacity) {
            throw new IllegalArgumentException("Enrollment must be between 0 and " + maxCapacity);
        }
        this.currentEnrollment = currentEnrollment; 
    }

    /**
     * Generates the table header for displaying classes.
     */
    public static String getTableHeader() {
        return String.format(
            "\n+------+-----------------+----------------------+----------+---------------------+----------------+----------------+%n" +
            "| %-4s | %-15s | %-20s | %-8s | %-19s | %-14s | %-14s |%n" +
            "+------+-----------------+----------------------+----------+---------------------+----------------+----------------+",
            "ID", "Name", "Type", "Trainer", "Schedule", "Duration (min)", "Capacity"
        );
    }

    /**
     * Generates the table footer for displaying classes.
     */
    public static String getTableFooter() {
        return "+------+-----------------+----------------------+----------+---------------------+----------------+----------------+\n";
    }

    /**
     * Formats class data into a table row.
     */
    public String toTableRow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String scheduleStr = (schedule != null) ? schedule.format(formatter) : "N/A";
        
        return String.format(
            "| %-4d | %-15s | %-20s | %-8d | %-19s | %-14d | %-14s |",
            id,
            name,
            type,
            trainerId,
            scheduleStr,
            durationMinutes,
            currentEnrollment + "/" + maxCapacity
        );
    }
}
