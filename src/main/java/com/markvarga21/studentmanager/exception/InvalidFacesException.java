package com.markvarga21.studentmanager.exception;

/**
 * A custom exception for when the face data is invalid.
 */
public class InvalidFacesException extends RuntimeException {
    /**
     * The default constructor.
     *
     * @param message The message to be sent when the exception occurs.
     */
    public InvalidFacesException(final String message) {
        super(message);
    }
}
