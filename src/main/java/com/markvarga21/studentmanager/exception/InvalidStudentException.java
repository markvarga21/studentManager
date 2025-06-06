package com.markvarga21.studentmanager.exception;

import com.markvarga21.studentmanager.util.Generated;

/**
 * A custom exception class which is used when the students data is invalid.
 */
@Generated
public class InvalidStudentException extends RuntimeException {
    /**
     * Constructor with a message as parameter.
     *
     * @param message The custom message.
     */
    public InvalidStudentException(final String message) {
        super(message);
    }
}
