package com.markvarga21.studentmanager.exception;

/**
 * An exception which is thrown when the image type is invalid.
 */
public class InvalidImageTypeException extends RuntimeException {
    /**
     * The constructor for the InvalidImageTypeException.
     *
     * @param message The message to be displayed.
     */
    public InvalidImageTypeException(final String message) {
        super(message);
    }
}
