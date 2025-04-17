package com.gymmanagement.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for managing PostgreSQL database connections and executing SQL commands from files.
 */
public class DatabaseConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/gym_management";
    private static final String USER = "dataadmin";
    private static final String PASSWORD = "password";

    /**
     * Default private constructor to prevent instantiation.
     */
    private DatabaseConfig() {}

    /**
     * Establishes a connection to the PostgreSQL database.
     * 
     * @return A {@link Connection} instance for interacting with the database.
     * @throws SQLException If the database driver is not found or the connection fails.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found.", e);
        }
    }

    /**
     * Executes SQL commands from a specified file.
     * 
     * @param filePath The path to the SQL file.
     * @throws SQLException If a database error occurs during SQL execution.
     * @throws IOException If an error occurs while reading the SQL file.
     * @throws IllegalArgumentException If the file path is null or empty.
     */
    public static void executeSqlFile(String filePath) throws SQLException, IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty.");
        }

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = Files.readString(Paths.get(filePath));
            stmt.execute(sql);
            System.out.println("Executed SQL file: " + filePath);
        }
    }
}
