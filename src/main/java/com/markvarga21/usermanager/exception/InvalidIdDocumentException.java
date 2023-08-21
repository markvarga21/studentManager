package com.markvarga21.usermanager.exception;

/**
 * A custom exception which is used when an error occurs when
 * processing the ID document or passport.
 */
public class InvalidIdDocumentException extends RuntimeException {
    /**
     * Constructor with a message as parameter.
     * 
     * @param message the custom message.
     */
    public InvalidIdDocumentException(final String message) {
        super(message);
    }
}
