package com.gymmanagement.service;

import java.util.List;
import com.gymmanagement.dao.WorkoutClassDAO;
import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.WorkoutClass;

public class WorkoutClassService {
    private final WorkoutClassDAO workoutClassDAO;

    public WorkoutClassService() {
        this.workoutClassDAO = new WorkoutClassDAO();
    }

    public List<WorkoutClass> getAllClasses() {
        try {
            return workoutClassDAO.findAll();
        } catch (DatabaseException e) {
            System.err.println("Error retrieving all classes: " + e.getMessage());
            return List.of(); // Return an empty list on error
        }
    }

    public List<WorkoutClass> getClassesByTrainer(int trainerId) {
        try {
            return workoutClassDAO.findByTrainerId(trainerId);
        } catch (DatabaseException e) {
            System.err.println("Error retrieving classes for trainer: " + e.getMessage());
            return List.of(); // Return an empty list on error
        }
    }
}