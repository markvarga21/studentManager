package com.markvarga21.studentmanager.exception.util;

import com.markvarga21.studentmanager.exception.OperationType;
import com.markvarga21.studentmanager.util.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * A custom class for sending useful information
 * in a {@code ResponseEntity} when
 * an exception occurs in the application.
 */
@Data
@AllArgsConstructor
@Generated
public class ApiError {
    /**
     * The date when the error occurred.
     */
    private Date timeStamp;

    /**
     * The status code of the error.
     */
    private HttpStatus status;

    /**
     * A custom message for the error.
     */
    private String message;

    /**
     * The operation type when the exception occurred.
     */
    private OperationType operationType;

    /**
     * The full stacktrace of the exception.
     */
    private String stackTrace;
}
