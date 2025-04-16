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

/**
 * Handles database operations for Workout Classes.
 */
public class WorkoutClassDAO {
    @SuppressWarnings("unused")
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
            throw new DatabaseException("Failed to retrieve classes", e);
        }
    }

    public List<WorkoutClass> findByTrainerId(int trainerId) throws DatabaseException {
        String sql = "SELECT * FROM workout_classes WHERE trainer_id = ?";
        List<WorkoutClass> classes = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, trainerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    classes.add(mapResultSetToWorkoutClass(rs));
                }
            }
            return classes;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find trainer's classes", e);
        }
    }

    public WorkoutClass findById(int classId) throws DatabaseException {
        String sql = "SELECT * FROM workout_classes WHERE class_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, classId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToWorkoutClass(rs) : null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find class", e);
        }
    }

    public List<WorkoutClass> findClassesByUserId(int userId) throws SQLException {
        List<WorkoutClass> enrolledClasses = new ArrayList<>();
        String query = "SELECT wc.class_id, wc.name, wc.description, wc.type, wc.trainer_id, " +
                    "wc.schedule, wc.duration_minutes, wc.max_capacity, wc.current_enrollment " +
                    "FROM workout_classes wc " +
                    "JOIN class_enrollments ce ON wc.class_id = ce.class_id " +
                       "WHERE ce.member_id = ?"; // Ensuring we fetch classes by member_id
    
        try (Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
    
            // Bind the userId to the query
            stmt.setInt(1, userId);
    
            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Map the result set to a WorkoutClass object
                    WorkoutClass wc = mapResultSetToWorkoutClass(rs);
                    enrolledClasses.add(wc);
                }
            }
        }
        // Return the list of enrolled classes
        return enrolledClasses;
    }
    

    public boolean create(WorkoutClass wc) throws SQLException {
        String sql = "INSERT INTO workout_classes (name, description, type, trainer_id, schedule, duration_minutes, max_capacity) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, wc.getName());
            stmt.setString(2, wc.getDescription());
            stmt.setString(3, wc.getType());
            stmt.setInt(4, wc.getTrainerId());
            stmt.setTimestamp(5, Timestamp.valueOf(wc.getSchedule()));
            stmt.setInt(6, wc.getDurationMinutes());
            stmt.setInt(7, wc.getMaxCapacity());
    
            return stmt.executeUpdate() > 0;
        }
    }
    

    public boolean update(WorkoutClass wc) throws DatabaseException {
        String sql = "UPDATE workout_classes SET name = ?, description = ?, type = ?, schedule = ?, "
                   + "duration_minutes = ?, max_capacity = ? WHERE class_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            setClassParameters(stmt, wc);
            stmt.setInt(7, wc.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update class", e);
        }
    }

    public boolean delete(int classId) throws DatabaseException {
        String sql = "DELETE FROM workout_classes WHERE class_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, classId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete class", e);
        }
    }

    /**
     * Enrolls member if class has capacity (transactional).
     * @return false if class is full or doesn't exist
     */
    public boolean enrollMember(int memberId, int classId) throws DatabaseException {
        String checkSql = "SELECT current_enrollment, max_capacity FROM workout_classes WHERE class_id = ?";
        String enrollSql = "INSERT INTO class_enrollments (member_id, class_id) VALUES (?, ?)";
        String updateSql = "UPDATE workout_classes SET current_enrollment = current_enrollment + 1 WHERE class_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            
            // Check capacity
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, classId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next() || rs.getInt("current_enrollment") >= rs.getInt("max_capacity")) {
                        return false;
                    }
                }
            }
            
            // Execute enrollment
            try (PreparedStatement enrollStmt = conn.prepareStatement(enrollSql)) {
                enrollStmt.setInt(1, memberId);
                enrollStmt.setInt(2, classId);
                enrollStmt.executeUpdate();
            }
            
            // Update count
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, classId);
                updateStmt.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            throw new DatabaseException("Enrollment failed", e);
        }
    }

    private void setClassParameters(PreparedStatement stmt, WorkoutClass wc) throws SQLException {
        stmt.setString(1, wc.getName());
        stmt.setString(2, wc.getDescription());
        stmt.setString(3, wc.getType());
        stmt.setTimestamp(4, Timestamp.valueOf(wc.getSchedule()));
        stmt.setInt(5, wc.getDurationMinutes());
        stmt.setInt(6, wc.getMaxCapacity());
    }

    private WorkoutClass mapResultSetToWorkoutClass(ResultSet rs) throws SQLException {
        WorkoutClass wc = new WorkoutClass();
        wc.setId(rs.getInt("class_id"));
        wc.setName(rs.getString("name"));
        wc.setDescription(rs.getString("description"));
        wc.setType(rs.getString("type"));
        wc.setTrainerId(rs.getInt("trainer_id"));
        wc.setSchedule(rs.getTimestamp("schedule").toLocalDateTime());
        wc.setDurationMinutes(rs.getInt("duration_minutes"));
        wc.setMaxCapacity(rs.getInt("max_capacity"));
        wc.setCurrentEnrollment(rs.getInt("current_enrollment"));
        return wc;
    }
}

