package com.markvarga21.usermanager.exception;

/**
 * A custom exception class which is used when the users data are invalid.
 */
public class InvalidUserException extends RuntimeException {
    /**
     * Constructor with a message as parameter.
     * @param message the custom message.
     */
    public InvalidUserException(final String message) {
        super(message);
    }
}
