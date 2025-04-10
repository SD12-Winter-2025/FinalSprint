package com.gymmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.gymmanagement.config.DatabaseConfig;
import com.gymmanagement.model.WorkoutClass;

public class WorkoutClassDAO {

public List<WorkoutClass> findAll() throws SQLException {
    String sql = "SELECT * FROM workout_classes";
    List<WorkoutClass> classes = new ArrayList<>();
    
    try (Connection conn = DatabaseConfig.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            classes.add(mapResultSetToWorkoutClass(rs));
        } 
        return classes;
    }
}

public List<WorkoutClass> findByTrainerId(int trainerId) throws SQLException {
    String sql = "SELECT * FROM workout_classes WHERE trainer_id = ?";
    List<WorkoutClass> classes = new ArrayList<>();
    
    try (Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, trainerId);
        ResultSet rs = stmt.executeQuery();
    
        while (rs.next()) {
            classes.add(mapResultSetToWorkoutClass(rs));
        }
        return classes;
    }
}

private WorkoutClass mapResultSetToWorkoutClass(ResultSet rs) throws SQLException {
    WorkoutClass workoutClass = new WorkoutClass();
    workoutClass.setId(rs.getInt("class_id"));
    workoutClass.setName(rs.getString("name"));
    workoutClass.setDescription(rs.getString("description"));
    workoutClass.setType(rs.getString("type"));
    workoutClass.setTrainerId(rs.getInt("trainer_id"));
    workoutClass.setSchedule(rs.getTimestamp("schedule").toLocalDateTime());
    workoutClass.setDurationMinutes(rs.getInt("duration_minutes"));
    workoutClass.setMaxCapacity(rs.getInt("max_capacity"));
    return workoutClass;
}


}