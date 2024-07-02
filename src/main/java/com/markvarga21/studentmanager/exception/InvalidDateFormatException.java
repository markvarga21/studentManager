package com.markvarga21.studentmanager.exception;

import com.markvarga21.studentmanager.util.Generated;

/**
 * A custom exception for when the date format is invalid.
 */
@Generated
public class InvalidDateFormatException extends RuntimeException {
    /**
     * The default constructor.
     *
     * @param message The message to be sent when the exception occurs.
     */
    public InvalidDateFormatException(final String message) {
        super(message);
    }
}
