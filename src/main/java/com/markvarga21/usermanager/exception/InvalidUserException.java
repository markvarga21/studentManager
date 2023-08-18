package com.markvarga21.usermanager.exception;

/**
 * A custom exception class which is used when the users data are invalid.
 */
public class InvalidUserException extends RuntimeException {
    public InvalidUserException(String message) {
        super(message);
    }
}
