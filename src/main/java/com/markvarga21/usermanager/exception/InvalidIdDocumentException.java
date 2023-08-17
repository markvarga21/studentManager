package com.markvarga21.usermanager.exception;

/**
 * A custom exception which is used when an error occurs when
 * processing the ID document or passport.
 */
public class InvalidIdDocumentException extends RuntimeException {
    public InvalidIdDocumentException(String message) {
        super(message);
    }
}
