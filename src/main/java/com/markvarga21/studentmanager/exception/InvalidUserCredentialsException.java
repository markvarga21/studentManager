package com.markvarga21.studentmanager.exception;

/**
 * Custom exception when the user credentials are invalid.
 */
public class InvalidUserCredentialsException extends RuntimeException {
    /**
     * Constructor for the InvalidUserCredentialsException class.
     *
     * @param message The message to display when the exception is thrown.
     */
    public InvalidUserCredentialsException(final String message) {
        super(message);
    }
}
