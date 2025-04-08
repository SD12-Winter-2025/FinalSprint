package com.gymmanagement.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConfig {

    private static final String URL = "jdbc:postgresql://localhost:5432/gym_management"; 
    private static final String USER = "dataadmin"; 
    private static final String PASSWORD = "password"; 

    public static Connection getConnection() throws SQLException { 

        return DriverManager.getConnection(URL, USER, PASSWORD); 

    }

}