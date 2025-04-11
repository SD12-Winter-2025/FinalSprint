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
            return List.of();
        }
    }


    public List<WorkoutClass> getClassesByTrainer(int trainerId) {

        try {
            return workoutClassDAO.findByTrainerId(trainerId);
        } catch (DatabaseException e) {
            System.err.println("Error retrieving classes for trainer: " + e.getMessage());
            return List.of();
        }
    }

    public boolean createClass(WorkoutClass workoutClass) {

        try {
            return workoutClassDAO.create(workoutClass);
        } catch (DatabaseException e) {
            System.err.println("Error creating class: " + e.getMessage());
            return false;
        }
    }

    public WorkoutClass getClassById(int classId) {

        try {
            return workoutClassDAO.findById(classId);
        } catch (DatabaseException e) {
            System.err.println("Error retrieving class by ID: " + e.getMessage());
            return null;
        }
    }

    public boolean updateClass(WorkoutClass workoutClass) {

        try {
            return workoutClassDAO.update(workoutClass);
        } catch (DatabaseException e) {
            System.err.println("Error updating class: " + e.getMessage());
            return false;
        }
    }


    public boolean deleteClass(int classId) {

        try {
            return workoutClassDAO.delete(classId);
        } catch (DatabaseException e) {
            System.err.println("Error deleting class: " + e.getMessage());
            return false;
        }
    }
}