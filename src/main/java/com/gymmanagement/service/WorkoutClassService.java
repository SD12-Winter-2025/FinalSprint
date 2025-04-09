package com.gymmanagement.service;

import java.util.List;
import com.gymmanagement.dao.WorkoutClassDAO;
import com.gymmanagement.model.WorkoutClass;


public class WorkoutClassService {

    private final WorkoutClassDAO workoutClassDAO;

    public WorkoutClassService() {
        this.workoutClassDAO = new WorkoutClassDAO();
    }

    public List<WorkoutClass> getAllClasses() {
        return workoutClassDAO.findAll(); // Direct call without error handling
    }
}