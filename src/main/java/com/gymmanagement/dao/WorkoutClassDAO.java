package com.gymmanagement.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gymmanagement.config.DatabaseConfig;
import com.gymmanagement.exception.DatabaseException;
import com.gymmanagement.model.WorkoutClass;

public class WorkoutClassDAO {
    private static final Logger logger = LoggerFactory.getLogger(WorkoutClassDAO.class);

    public List<WorkoutClass> findAll() throws DatabaseException {
        String sql = "SELECT * FROM workout_classes";
        List<WorkoutClass> classes = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                classes.add(mapResultSetToWorkoutClass(rs));
            }
            return classes;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve all workout classes", e);
        }
    }

    public List<WorkoutClass> findByTrainerId(int trainerId) throws DatabaseException {
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
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find classes for trainer ID: " + trainerId, e);
        }
    }

    public WorkoutClass findById(int classId) throws DatabaseException {
        String sql = "SELECT * FROM workout_classes WHERE class_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToWorkoutClass(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find workout class with ID: " + classId, e);
        }
    }

    public boolean create(WorkoutClass workoutClass) throws DatabaseException {
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
        } catch (SQLException e) {
            throw new DatabaseException("Failed to create workout class: " + workoutClass.getName(), e);
        }
    }

    public boolean update(WorkoutClass workoutClass) throws DatabaseException {
        String sql = "UPDATE workout_classes SET name = ?, description = ?, type = ?, schedule = ?, " +
                    "duration_minutes = ?, max_capacity = ? WHERE class_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, workoutClass.getName());
            stmt.setString(2, workoutClass.getDescription());
            stmt.setString(3, workoutClass.getType());
            stmt.setTimestamp(4, Timestamp.valueOf(workoutClass.getSchedule()));
            stmt.setInt(5, workoutClass.getDurationMinutes());
            stmt.setInt(6, workoutClass.getMaxCapacity());
            stmt.setInt(7, workoutClass.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update workout class with ID: " + workoutClass.getId(), e);
        }
    }

    public boolean delete(int classId) throws DatabaseException {
        String sql = "DELETE FROM workout_classes WHERE class_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, classId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete workout class with ID: " + classId, e);
        }
    }

    public boolean enrollMember(int memberId, int classId) throws DatabaseException {
        String checkSql = "SELECT current_enrollment, max_capacity FROM workout_classes WHERE class_id = ?";
        String enrollSql = "INSERT INTO class_enrollments (member_id, class_id) VALUES (?, ?)";
        String updateSql = "UPDATE workout_classes SET current_enrollment = current_enrollment + 1 WHERE class_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            
            // Check capacity
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, classId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (!rs.next()) {
                    return false;
                }
                
                int currentEnrollment = rs.getInt("current_enrollment");
                int maxCapacity = rs.getInt("max_capacity");
                
                if (currentEnrollment >= maxCapacity) {
                    return false;
                }
            }
            
            // Enroll member
            try (PreparedStatement enrollStmt = conn.prepareStatement(enrollSql)) {
                enrollStmt.setInt(1, memberId);
                enrollStmt.setInt(2, classId);
                enrollStmt.executeUpdate();
            }
            
            // Update enrollment count
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, classId);
                updateStmt.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to enroll member with ID: " + memberId + " into class ID: " + classId, e);
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
        workoutClass.setCurrentEnrollment(rs.getInt("current_enrollment"));
        return workoutClass;
    }
}