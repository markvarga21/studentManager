package com.markvarga21.studentmanager.exception;

/**
 * A custom exception which is used when an error occurs when
 * processing the passport.
 */
public class InvalidPassportException extends RuntimeException {
    /**
     * Constructor with a message as parameter.
     *
     * @param message The custom message.
     */
    public InvalidPassportException(final String message) {
        super(message);
    }
}
