package com.markvarga21.studentmanager.exception;

import com.markvarga21.studentmanager.util.Generated;

/**
 * A custom exception which is used if an error occurs when
 * processing the passport.
 */
@Generated
public class InvalidPassportException extends RuntimeException {
    /**
     * Constructor with a feedback message.
     *
     * @param message The custom message.
     */
    public InvalidPassportException(final String message) {
        super(message);
    }
}
