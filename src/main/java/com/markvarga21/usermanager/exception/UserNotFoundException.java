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
    private final OperationType type;

    /**
     * Custom constructor which contains a message and the
     * operation type of the exception.
     *
     * @param message a custom message for alerting the lack of a user.
     * @param type the type of the operation.
     */
    public UserNotFoundException(
            final String message,
            final OperationType type
    ) {
        super(message);
        this.type = type;
    }
}

