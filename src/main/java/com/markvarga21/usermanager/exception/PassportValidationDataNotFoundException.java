package com.markvarga21.usermanager.exception;

public class PassportValidationDataNotFoundException extends RuntimeException {
    public PassportValidationDataNotFoundException(final String message) {
        super(message);
    }
}
