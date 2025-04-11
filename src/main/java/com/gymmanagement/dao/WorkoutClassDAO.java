package com.gymmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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

public boolean create(WorkoutClass workoutClass) throws SQLException {
    String sql = "INSERT INTO workout_classes (name, description, type, trainer_id, schedule, duration_minutes, max_capacity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        stmt.setString(1, workoutClass.getName());
        stmt.setString(2, workoutClass.getDescription());
        stmt.setString(3, workoutClass.getType());
        stmt.setInt(4, workoutClass.getTrainerId());
        stmt.setTimestamp(5, Timestamp.valueOf(workoutClass.getSchedule()));
        stmt.setInt(6, workoutClass.getDurationMinutes());
        stmt.setInt(7, workoutClass.getMaxCapacity());
        
        int affectedRows = stmt.executeUpdate();
        if (affectedRows > 0) {
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    workoutClass.setId(rs.getInt(1));
                }
            }
            return true;
        }
        return false;
    }
}

public boolean delete(int classId) throws SQLException {
    String sql = "DELETE FROM workout_classes WHERE class_id = ?";
    
    try (Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, classId);
        return stmt.executeUpdate() > 0;
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