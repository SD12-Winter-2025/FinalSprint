package com.gymmanagement.service;

import java.sql.SQLException;
import java.util.List;

import com.gymmanagement.dao.WorkoutClassDAO;
import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.WorkoutClass;

/**
 * Handles workout class operations including scheduling and enrollment.
 */
public class WorkoutClassService {
    private final WorkoutClassDAO workoutClassDAO;

    public WorkoutClassService() {
        this.workoutClassDAO = new WorkoutClassDAO();
    }

    public List<WorkoutClass> getAllClasses() {
        try {
            return workoutClassDAO.findAll();
        } catch (DatabaseException e) {
            System.err.println("Failed to get classes: " + e.getMessage());
            return List.of();
        }
    }

    public List<WorkoutClass> getClassesByTrainer(int trainerId) {
        try {
            return workoutClassDAO.findByTrainerId(trainerId);
        } catch (DatabaseException e) {
            System.err.println("Failed to get trainer classes: " + e.getMessage());
            return List.of();
        }
    }

    public boolean createClass(WorkoutClass workoutClass) {
        try {
            return workoutClassDAO.create(workoutClass);
        } catch (SQLException e) {
            System.err.println("Error creating class: " + e.getMessage());
            return false;
        }
    }
    

    public WorkoutClass getClassById(int classId) {
        try {
            return workoutClassDAO.findById(classId);
        } catch (DatabaseException e) {
            System.err.println("Failed to get class: " + e.getMessage());
            return null;
        }
    }

    public boolean updateClass(WorkoutClass workoutClass) {
        try {
            return workoutClassDAO.update(workoutClass);
        } catch (DatabaseException e) {
            System.err.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteClass(int classId) {
        try {
            return workoutClassDAO.delete(classId);
        } catch (DatabaseException e) {
            System.err.println("Delete failed: " + e.getMessage());
            return false;
        }
    }

    public boolean enrollMember(int memberId, int classId) {
        try {
            return workoutClassDAO.enrollMember(memberId, classId);
        } catch (DatabaseException e) {
            System.err.println("Enrollment failed: " + e.getMessage());
            return false;
        }
    }

    public List<WorkoutClass> getEnrolledClasses(int userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID provided");
        }
        return workoutClassDAO.findClassesByUserId(userId);
    }
    

}