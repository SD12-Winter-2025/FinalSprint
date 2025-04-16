package com.gymmanagement.exception;

/**
 * Wraps database-related exceptions with user-friendly messages.
 */
public class DatabaseException extends Exception {
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DatabaseException(String message) {
        super(message);
    }
}