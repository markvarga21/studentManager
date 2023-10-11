package com.markvarga21.usermanager.exception;

/**
 * A custom exception for when the date format is invalid.
 */
public class InvalidDateFormatException extends RuntimeException {
    /**
     * The default constructor.
     *
     * @param message The message to be sent when the exception occurs.
     */
    public InvalidDateFormatException(final String message) {
        super(message);
    }
}
