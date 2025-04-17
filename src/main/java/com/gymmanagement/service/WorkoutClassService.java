package com.gymmanagement.service;

import java.util.List;

import com.gymmanagement.dao.WorkoutClassDAO;
import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.WorkoutClass;

/**
 * Service class for managing workout class operations.
 * 
 * <p>Provides functionalities for creating, updating, retrieving, deleting,
 * and enrolling members in workout classes. It also handles fetching workout
 * classes assigned to trainers or enrolled by members.</p>
 */
public class WorkoutClassService {
    private final WorkoutClassDAO workoutClassDAO;

    /**
     * Constructs a {@link WorkoutClassService} instance to manage workout class operations.
     */
    public WorkoutClassService() {
        this.workoutClassDAO = new WorkoutClassDAO();
    }

    /**
     * Retrieves all workout classes from the system.
     * 
     * @return A {@link List} of {@link WorkoutClass} objects representing all workout classes.
     *         Returns an empty list if no classes are found or an error occurs.
     */
    public List<WorkoutClass> getAllClasses() {
        try {
            return workoutClassDAO.findAll();
        } catch (DatabaseException e) {
            System.err.println("Failed to get classes: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Retrieves workout classes assigned to a specific trainer.
     * 
     * @param trainerId The ID of the trainer.
     * @return A {@link List} of {@link WorkoutClass} objects representing the trainer's classes.
     *         Returns an empty list if no classes are found or an error occurs.
     */
    public List<WorkoutClass> getClassesByTrainer(int trainerId) {
        try {
            return workoutClassDAO.findByTrainerId(trainerId);
        } catch (DatabaseException e) {
            System.err.println("Failed to get trainer classes: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Creates a new workout class in the system.
     * 
     * @param workoutClass The {@link WorkoutClass} object containing class details.
     * @return {@code true} if the class is created successfully, {@code false} otherwise.
     * @throws DatabaseException If a database error occurs during class creation.
     */
    public boolean createClass(WorkoutClass workoutClass) throws DatabaseException {
        try {
            return workoutClassDAO.create(workoutClass);
        } catch (DatabaseException e) {
            throw new DatabaseException("Error creating class", e);
        }
    }

    /**
     * Retrieves a workout class by its ID.
     * 
     * @param classId The ID of the class to retrieve.
     * @return A {@link WorkoutClass} object representing the class if found, or {@code null} if not found.
     * @throws DatabaseException If a database error occurs during retrieval.
     */
    public WorkoutClass getClassById(int classId) throws DatabaseException {
        try {
            return workoutClassDAO.findById(classId);
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to get class with ID: " + classId, e);
        }
    }

    /**
     * Updates the details of an existing workout class.
     * 
     * @param workoutClass The {@link WorkoutClass} object containing updated class details.
     * @return {@code true} if the class is updated successfully, {@code false} otherwise.
     * @throws DatabaseException If a database error occurs during the update process.
     */
    public boolean updateClass(WorkoutClass workoutClass) throws DatabaseException {
        try {
            return workoutClassDAO.update(workoutClass);
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to update class with ID: " + workoutClass.getId(), e);
        }
    }

    /**
     * Deletes a workout class from the system.
     * 
     * @param classId The ID of the class to delete.
     * @return {@code true} if the class is deleted successfully, {@code false} otherwise.
     * @throws DatabaseException If a database error occurs during deletion.
     */
    public boolean deleteClass(int classId) throws DatabaseException {
        try {
            return workoutClassDAO.delete(classId);
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to delete class with ID: " + classId, e);
        }
    }

    /**
     * Enrolls a member in a workout class.
     * 
     * @param memberId The ID of the member to enroll.
     * @param classId The ID of the class in which to enroll the member.
     * @return {@code true} if the enrollment is successful, {@code false} otherwise.
     * @throws DatabaseException If a database error occurs during enrollment.
     */
    public boolean enrollMember(int memberId, int classId) throws DatabaseException {
        try {
            return workoutClassDAO.enrollMember(memberId, classId);
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to enroll member with ID: " + memberId + " into class ID: " + classId, e);
        }
    }

    /**
     * Retrieves all workout classes in which a specific member is enrolled.
     * 
     * @param userId The ID of the member.
     * @return A {@link List} of {@link WorkoutClass} objects representing the member's enrolled classes.
     * @throws DatabaseException If a database error occurs during retrieval.
     * @throws IllegalArgumentException If the provided user ID is invalid.
     */
    public List<WorkoutClass> getEnrolledClasses(int userId) throws DatabaseException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID provided: " + userId);
        }
        try {
            return workoutClassDAO.findClassesByUserId(userId);
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to retrieve enrolled classes for user ID: " + userId, e);
        }
    }
}
