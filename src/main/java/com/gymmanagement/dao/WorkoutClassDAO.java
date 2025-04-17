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
 * DAO (Data Access Object) class for handling database operations on Workout Classes.
 * Provides methods for CRUD operations, enrollment management, and retrieval of workout class data.
 */
public class WorkoutClassDAO {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutClassDAO.class);

    /**
     * Default constructor for creating an instance of WorkoutClassDAO.
     */
    public WorkoutClassDAO() {}

    /**
     * Retrieves all workout classes from the database.
     * 
     * @return A {@link List} of {@link WorkoutClass} objects representing all workout classes.
     * @throws DatabaseException If a database access error occurs or the operation fails.
     */
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
            logger.error("Failed to retrieve all workout classes", e);
            throw new DatabaseException("Failed to retrieve classes", e);
        }
    }

    /**
     * Retrieves workout classes assigned to a specific trainer.
     * 
     * @param trainerId The ID of the trainer whose classes are to be retrieved.
     * @return A {@link List} of {@link WorkoutClass} objects assigned to the trainer.
     * @throws DatabaseException If a database access error occurs or the operation fails.
     */
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
            logger.error("Failed to retrieve classes for trainer ID: {}", trainerId, e);
            throw new DatabaseException("Failed to find trainer's classes", e);
        }
    }

    /**
     * Retrieves a specific workout class by its ID.
     * 
     * @param classId The ID of the class to retrieve.
     * @return A {@link WorkoutClass} object representing the class, or {@code null} if no class exists with the given ID.
     * @throws DatabaseException If a database access error occurs or the operation fails.
     */
    public WorkoutClass findById(int classId) throws DatabaseException {
        String sql = "SELECT * FROM workout_classes WHERE class_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, classId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToWorkoutClass(rs) : null;
            }
        } catch (SQLException e) {
            logger.error("Failed to find workout class ID: {}", classId, e);
            throw new DatabaseException("Failed to find class", e);
        }
    }

    /**
     * Retrieves workout classes in which a specific member is enrolled.
     * 
     * @param userId The ID of the member whose enrolled classes are to be retrieved.
     * @return A {@link List} of {@link WorkoutClass} objects representing the member's enrolled classes.
     * @throws DatabaseException If a database access error occurs or the operation fails.
     */
    public List<WorkoutClass> findClassesByUserId(int userId) throws DatabaseException {
        List<WorkoutClass> enrolledClasses = new ArrayList<>();
        String query = "SELECT wc.class_id, wc.name, wc.description, wc.type, wc.trainer_id, " +
                       "wc.schedule, wc.duration_minutes, wc.max_capacity, wc.current_enrollment " +
                       "FROM workout_classes wc " +
                       "JOIN class_enrollments ce ON wc.class_id = ce.class_id " +
                       "WHERE ce.member_id = ?";
    
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    enrolledClasses.add(mapResultSetToWorkoutClass(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve enrolled classes for user ID: {}", userId, e);
            throw new DatabaseException("Failed to retrieve enrolled classes", e);
        }
        return enrolledClasses;
    }

    /**
     * Creates a new workout class in the database.
     * 
     * @param wc The {@link WorkoutClass} object containing class details to insert into the database.
     * @return {@code true} if the class was created successfully, {@code false} otherwise.
     * @throws DatabaseException If a database access error occurs or the operation fails.
     */
    public boolean create(WorkoutClass wc) throws DatabaseException {
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
        } catch (SQLException e) {
            logger.error("Failed to create workout class", e);
            throw new DatabaseException("Failed to create workout class", e);
        }
    }

    /**
     * Updates an existing workout class in the database.
     * 
     * @param wc The {@link WorkoutClass} object containing updated class details.
     * @return {@code true} if the class was updated successfully, {@code false} otherwise.
     * @throws DatabaseException If a database access error occurs or the operation fails.
     */
    public boolean update(WorkoutClass wc) throws DatabaseException {
        String sql = "UPDATE workout_classes SET name = ?, description = ?, type = ?, schedule = ?, "
                   + "duration_minutes = ?, max_capacity = ? WHERE class_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            setClassParameters(stmt, wc);
            stmt.setInt(7, wc.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Failed to update workout class ID: {}", wc.getId(), e);
            throw new DatabaseException("Failed to update class", e);
        }
    }

    /**
     * Deletes a workout class from the database by its class ID.
     * 
     * @param classId The ID of the class to delete.
     * @return {@code true} if the class was deleted successfully, {@code false} otherwise.
     * @throws DatabaseException If a database access error occurs or the operation fails.
     */
    public boolean delete(int classId) throws DatabaseException {
        String sql = "DELETE FROM workout_classes WHERE class_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, classId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Failed to delete workout class ID: {}", classId, e);
            throw new DatabaseException("Failed to delete class", e);
        }
    }

    /**
     * Enrolls a member in a workout class if there is available capacity.
     * Performs a capacity check, enrolls the member, and updates the current enrollment count.
     * 
     * @param memberId The ID of the member to enroll.
     * @param classId The ID of the class in which to enroll the member.
     * @return {@code true} if the member was enrolled successfully, {@code false} otherwise.
     * @throws DatabaseException If a database access error occurs or the operation fails.
     */
    public boolean enrollMember(int memberId, int classId) throws DatabaseException {
        String checkSql = "SELECT current_enrollment, max_capacity FROM workout_classes WHERE class_id = ?";
        String enrollSql = "INSERT INTO class_enrollments (member_id, class_id) VALUES (?, ?)";
        String updateSql = "UPDATE workout_classes SET current_enrollment = current_enrollment + 1 WHERE class_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            // Perform a capacity check to ensure the class is not full
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, classId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next() || rs.getInt("current_enrollment") >= rs.getInt("max_capacity")) {
                        return false;
                    }
                }
            }

            // Execute the enrollment
            try (PreparedStatement enrollStmt = conn.prepareStatement(enrollSql)) {
                enrollStmt.setInt(1, memberId);
                enrollStmt.setInt(2, classId);
                enrollStmt.executeUpdate();
            }

            // Update the current enrollment count
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, classId);
                updateStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            logger.error("Enrollment failed for member ID: {}, class ID: {}", memberId, classId, e);
            throw new DatabaseException("Enrollment failed", e);
        }
    }

    /**
     * Sets the parameters of a {@link PreparedStatement} using values from a {@link WorkoutClass} object.
     * This method prepares the SQL statement with class details.
     * 
     * @param stmt The {@link PreparedStatement} to set parameters for.
     * @param wc The {@link WorkoutClass} object containing class details.
     * @throws SQLException If an error occurs while setting parameters.
     */
    private void setClassParameters(PreparedStatement stmt, WorkoutClass wc) throws SQLException {
        stmt.setString(1, wc.getName());
        stmt.setString(2, wc.getDescription());
        stmt.setString(3, wc.getType());
        stmt.setTimestamp(4, Timestamp.valueOf(wc.getSchedule()));
        stmt.setInt(5, wc.getDurationMinutes());
        stmt.setInt(6, wc.getMaxCapacity());
    }

    /**
     * Maps the result set of a database query to a {@link WorkoutClass} object.
     * Converts data retrieved from the database into a usable Java object.
     * 
     * @param rs The {@link ResultSet} containing class data.
     * @return A {@link WorkoutClass} object populated with the data from the result set.
     * @throws SQLException If an error occurs while reading the result set.
     */
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
