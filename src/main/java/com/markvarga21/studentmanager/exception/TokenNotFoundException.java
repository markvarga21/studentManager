package com.markvarga21.studentmanager.exception;

/**
 * Exception used if a token is not found in the database.
 */
public class TokenNotFoundException extends RuntimeException {
    /**
     * Constructs a new {@code TokenNotFoundException}
     * with the specified detail message.
     *
     * @param message the detailed message.
     */
    public TokenNotFoundException(final String message) {
        super(message);
    }
}
