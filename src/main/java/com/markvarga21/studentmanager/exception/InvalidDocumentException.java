package com.markvarga21.studentmanager.exception;

/**
 * A custom exception class which is used when the selfie is invalid.
 */
public class InvalidDocumentException extends RuntimeException {
    /**
     * Constructor with a message as parameter.
     *
     * @param message The custom message.
     */
    public InvalidDocumentException(final String message) {
        super(message);
    }
}
