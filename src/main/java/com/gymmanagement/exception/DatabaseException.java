package com.gymmanagement.exception;

/**
 * Represents exceptions related to database operations.
 * Provides constructors for wrapping error messages and underlying causes.
 */
public class DatabaseException extends Exception {

    /**
     * Constructs a database exception with a specific error message and cause.
     * 
     * @param message The detailed error message.
     * @param cause The underlying cause of the exception.
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a database exception with a specific error message.
     * 
     * @param message The detailed error message.
     */
    public DatabaseException(String message) {
        super(message);
    }
}
