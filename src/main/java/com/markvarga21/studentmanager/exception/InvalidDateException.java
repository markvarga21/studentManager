package com.markvarga21.studentmanager.exception;

import com.markvarga21.studentmanager.util.Generated;

/**
 * The InvalidDateException class is used to throw
 * an exception when a date is invalid.
 */
@Generated
public class InvalidDateException extends RuntimeException {
    /**
     * Constructs a new InvalidDateException with the
     * specified detail message.
     *
     * @param message The detail message.
     */
    public InvalidDateException(final String message) {
        super(message);
    }
}
