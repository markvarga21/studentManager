package com.markvarga21.studentmanager.exception;

import com.markvarga21.studentmanager.util.Generated;

/**
 * An exception which is thrown when a passport
 * cannot be found.
 */
@Generated
public class PassportNotFoundException extends RuntimeException {
    /**
     * Creates a new passport not found exception.
     *
     * @param message The message of the exception.
     */
    public PassportNotFoundException(final String message) {
        super(message);
    }
}
