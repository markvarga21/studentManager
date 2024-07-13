package com.markvarga21.studentmanager.exception;

/**
 * Exception thrown when a user is not found in the database.
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Constructor for the UserNotFoundException class.
     *
     * @param message The message to display.
     */
    public UserNotFoundException(final String message) {
        super(message);
    }
}
