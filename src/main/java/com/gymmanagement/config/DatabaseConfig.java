package com.gymmanagement.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages PostgreSQL connections and SQL script execution.
 */
public class DatabaseConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/gym_management";
    private static final String USER = "dataadmin";
    private static final String PASSWORD = "password";

    /**
     * @throws SQLException if driver not found or connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found", e);
        }
    }

    /**
     * Executes SQL from a file (for initialization).
     * @throws IllegalArgumentException for invalid paths
     */
    public static void executeSqlFile(String filePath) throws SQLException, IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid file path");
        }
        
        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute(Files.readString(Paths.get(filePath)));
            System.out.println("Executed: " + filePath);
        }
    }
}