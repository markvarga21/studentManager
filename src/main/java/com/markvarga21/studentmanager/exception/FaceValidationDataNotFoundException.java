package com.markvarga21.studentmanager.exception;

/**
 * A custom exception for when the face validation data
 * is not found in the database.
 */
public class FaceValidationDataNotFoundException extends RuntimeException {
    /**
     * The default constructor.
     *
     * @param message The message to be sent when the exception occurs.
     */
    public FaceValidationDataNotFoundException(final String message) {
        super(message);
    }
}
