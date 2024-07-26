package com.markvarga21.studentmanager.exception;

/**
 * Exception thrown when a token is not found in the database.
 */
public class TokenNotFoundException extends RuntimeException {
    /**
     * Constructs a new TokenNotFoundException with the specified detail message.
     *
     * @param message the detail message.
     */
    public TokenNotFoundException(final String message) {
        super(message);
    }
}
