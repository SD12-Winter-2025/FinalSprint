package com.gymmanagement.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {

    private static final String URL = "jdbc:postgresql://localhost:5432/gym_management";
    private static final String USER = "dataadmin";
    private static final String PASSWORD = "password";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found", e);
        }
    }

    public static void executeSqlFile(String filePath) throws SQLException, IOException {
        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {
            String sql = new String(Files.readAllBytes(Paths.get(filePath)));
            stmt.execute(sql);
        }
    }
}