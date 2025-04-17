package com.gymmanagement.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a scheduled fitness class with capacity limits and enrollment tracking.
 * Includes details such as class name, description, type, schedule, and trainer assignment.
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

    /**
     * Default constructor for creating a workout class with empty fields.
     */
    public WorkoutClass() {}

    /**
     * Retrieves the unique identifier for this workout class.
     * 
     * @return The unique class ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Updates the unique identifier for this workout class.
     * 
     * @param id New unique class ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the workout class.
     * 
     * @return The name of the class (e.g., "Morning Yoga").
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the workout class.
     * 
     * @param name New name for the workout class.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the description of the workout class.
     * 
     * @return A detailed description of the class.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the description of the workout class.
     * 
     * @param description New details about the class.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the category or type of the workout class.
     * 
     * @return The type of the class (e.g., "Cardio", "Strength").
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the category or type of the workout class.
     * 
     * @param type New type or category for the class.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the ID of the trainer assigned to this class.
     * 
     * @return The ID of the assigned trainer.
     */
    public int getTrainerId() {
        return trainerId;
    }

    /**
     * Updates the ID of the trainer assigned to this class.
     * 
     * @param trainerId The ID of the new assigned trainer.
     */
    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    /**
     * Retrieves the scheduled date and time of the workout class.
     * 
     * @return The scheduled date and time.
     */
    public LocalDateTime getSchedule() {
        return schedule;
    }

    /**
     * Updates the scheduled date and time of the workout class.
     * 
     * @param schedule The new scheduled date and time for the class.
     */
    public void setSchedule(LocalDateTime schedule) {
        this.schedule = schedule;
    }

    /**
     * Retrieves the duration of the workout class in minutes.
     * 
     * @return The duration of the class in minutes.
     */
    public int getDurationMinutes() {
        return durationMinutes;
    }

    /**
     * Sets the duration of the workout class.
     * Ensures that the duration is a positive value.
     * 
     * @param durationMinutes New duration for the class, in minutes.
     * @throws IllegalArgumentException If the duration is less than or equal to 0.
     */
    public void setDurationMinutes(int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive.");
        }
        this.durationMinutes = durationMinutes;
    }

    /**
     * Retrieves the maximum capacity for this workout class.
     * 
     * @return The maximum number of participants allowed in the class.
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Sets the maximum capacity for this workout class.
     * Ensures that the capacity is a positive value.
     * 
     * @param maxCapacity New maximum number of participants allowed in the class.
     * @throws IllegalArgumentException If the capacity is less than or equal to 0.
     */
    public void setMaxCapacity(int maxCapacity) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        this.maxCapacity = maxCapacity;
    }

    /**
     * Retrieves the current enrollment count for this workout class.
     * 
     * @return The current number of enrolled participants.
     */
    public int getCurrentEnrollment() {
        return currentEnrollment;
    }

    /**
     * Updates the enrollment count for this workout class.
     * Ensures that the enrollment is within the allowed range.
     * 
     * @param currentEnrollment New enrollment count (must be between 0 and maxCapacity).
     * @throws IllegalArgumentException If the enrollment count is outside the valid range.
     */
    public void setCurrentEnrollment(int currentEnrollment) {
        if (currentEnrollment < 0 || currentEnrollment > maxCapacity) {
            throw new IllegalArgumentException("Enrollment must be between 0 and " + maxCapacity);
        }
        this.currentEnrollment = currentEnrollment;
    }

    /**
     * Provides a formatted table header for displaying class information in a table.
     * 
     * @return A formatted string representing the table header.
     */
    public static String getTableHeader() {
        return String.format(
            "\n+------+-----------------+----------------------+----------+---------------------+----------------+----------------+%n" +
            "| %-4s | %-15s | %-20s | %-8s | %-19s | %-14s | %-14s |%n" +
            "+------+-----------------+----------------------+----------+---------------------+----------------+----------------+",
            "ID", "Name", "Type", "Trainer", "Schedule", "Duration (min)", "Capacity");
    }

    /**
     * Provides a formatted table footer for displaying class information.
     * 
     * @return A formatted string representing the table footer.
     */
    public static String getTableFooter() {
        return "+------+-----------------+----------------------+----------+---------------------+----------------+----------------+\n";
    }

    /**
     * Formats the details of the workout class into a table row.
     * 
     * @return A formatted string representing the class details in a table row.
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
            currentEnrollment + "/" + maxCapacity);
    }
}
