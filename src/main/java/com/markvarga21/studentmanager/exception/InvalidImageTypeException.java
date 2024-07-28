package com.markvarga21.studentmanager.exception;

import com.markvarga21.studentmanager.util.Generated;

/**
 * An exception which is thrown when the image type is invalid.
 */
@Generated
public class InvalidImageTypeException extends RuntimeException {
    /**
     * The constructor for the {@code InvalidImageTypeException} class.
     *
     * @param message The message to be displayed.
     */
    public InvalidImageTypeException(final String message) {
        super(message);
    }
}
