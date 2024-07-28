package com.markvarga21.studentmanager.exception;

import com.markvarga21.studentmanager.util.Generated;

/**
 * A custom exception class which is used if the selfie is invalid.
 */
@Generated
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
