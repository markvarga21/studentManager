package com.markvarga21.studentmanager.exception;

/**
 * The InvalidDateException class is used to throw
 * an exception when a date is invalid.
 */
public class InvalidDateException extends RuntimeException {
    /**
     * Constructs a new InvalidDateException with the
     * specified detail message.
     *
     * @param message The detail message.
     */
    public InvalidDateException(final String message) {
        super(message);
    }
}
