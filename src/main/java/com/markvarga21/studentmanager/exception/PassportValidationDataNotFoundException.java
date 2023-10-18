package com.markvarga21.studentmanager.exception;

/**
 * A custom exception which is used when the
 * passport validation data is not found.
 */
public class PassportValidationDataNotFoundException extends RuntimeException {
    /**
     * The default constructor.
     *
     * @param message The message to be sent when the exception occurs.
     */
    public PassportValidationDataNotFoundException(final String message) {
        super(message);
    }
}
