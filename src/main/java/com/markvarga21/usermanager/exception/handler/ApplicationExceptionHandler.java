package com.markvarga21.usermanager.exception.handler;

import com.markvarga21.usermanager.exception.ApiError;
import com.markvarga21.usermanager.exception.InvalidIdDocumentException;
import com.markvarga21.usermanager.exception.OperationType;
import com.markvarga21.usermanager.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * A custom exception handler for dealing with
 * certain exceptions in the applications.
 */
@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {
    /**
     * Handles if the user did not input the information correctly when using
     * the application's endpoints.
     *
     * @param ex the exception caused by the invalid field value.
     * @return a readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error(String.format("Invalid field(s): %s", errors));
        String message = String.format("Invalid field in creating the user! Violations: %s", errors);
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.BAD_REQUEST,
                message,
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles the exception if a user cannot be found in the application.
     *
     * @param ex the exception caused by not founding the user.
     * @return a readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found!");
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.NOT_FOUND,
                "User not found!",
                ex.getOperationType(),
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles if the format of the user's birthdate is invalid or not yet supported.
     *
     * @param ex the exception caused by incorrectly formatting the birthdate.
     * @return a readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleInvalidDateFormatException(DateTimeParseException ex) {
        log.error("Invalid date format!");
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.BAD_REQUEST,
                "Invalid date format! Allowed format is: YYYY-MM-DD",
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    /**
     * Handles the exception if the inputted gender is invalid.
     *
     * @param ex the exception caused by the invalid gender.
     * @return a readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidGenderException(HttpMessageNotReadableException ex) {
        String message = "Invalid gender type! Allowed gender types are: MALE or FEMALE";
        log.error(message);
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.BAD_REQUEST,
                message,
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(InvalidIdDocumentException.class)
    public ResponseEntity<Object> handleInvalidIdDocumentException(UserNotFoundException ex) {
        log.error("ID document not found!");
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.BAD_REQUEST,
                "ID document not found!!",
                ex.getOperationType(),
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    private String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
