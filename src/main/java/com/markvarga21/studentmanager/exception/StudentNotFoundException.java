package com.markvarga21.studentmanager.exception;

import com.markvarga21.studentmanager.util.Generated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A custom exception which is used when a student could not been found.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
@Generated
public class StudentNotFoundException extends RuntimeException {
    /**
     * The type of the operation the user just did,
     * which caused the exception to raise.
     */
    private final OperationType type;

    /**
     * Custom constructor which contains a message and the
     * operation type of the exception.
     *
     * @param message A custom message for alerting the lack of a user.
     * @param type The type of the operation.
     */
    public StudentNotFoundException(
            final String message,
            final OperationType type
    ) {
        super(message);
        this.type = type;
    }
}

