package com.markvarga21.usermanager.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A custom exception which is used when a user could not been found.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    /**
     * The type of the operation the user just did which caused the exception.
     */
    private OperationType operationType;

    /**
     * Custom constructor which contains a message and the
     * operation type of the exception.
     *
     * @param message a custom message for alerting the lack of a user.
     * @param operationType the type of the operation.
     */
    public UserNotFoundException(String message, OperationType operationType) {
        super(message);
        this.operationType = operationType;
    }
}

