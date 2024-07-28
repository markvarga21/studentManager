package com.markvarga21.studentmanager.exception;

/**
 * Exception used if a user is not found in the database.
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Constructor for the {@code UserNotFoundException} class.
     *
     * @param message The message to display.
     */
    public UserNotFoundException(final String message) {
        super(message);
    }
}
