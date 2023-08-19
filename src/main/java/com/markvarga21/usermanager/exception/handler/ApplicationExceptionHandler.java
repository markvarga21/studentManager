package com.markvarga21.usermanager.exception.handler;

import com.markvarga21.usermanager.exception.ApiError;
import com.markvarga21.usermanager.exception.InvalidIdDocumentException;
import com.markvarga21.usermanager.exception.InvalidUserException;
import com.markvarga21.usermanager.exception.OperationType;
import com.markvarga21.usermanager.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Object> handleValidationExceptions(
            final MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error(String.format("Invalid field(s): %s", errors));
        String message = String.format(
                "Invalid field in creating the user! Violations: %s",
                errors
        );
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.BAD_REQUEST,
                message,
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles if the input fields are invalid.
     *
     * @param ex the exception caused by a constraint violation.
     * @return a readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            final ConstraintViolationException ex
    ) {
        String message = String.format(
                "Invalid field in creating the user! Violations: %s",
                ex.getMessage()
        );
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.BAD_REQUEST,
                message,
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles the exception if a user cannot be found in the application.
     *
     * @param ex the exception caused by not founding the user.
     * @return a readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(
            final UserNotFoundException ex
    ) {
        log.error("User not found!");
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.NOT_FOUND,
                "User not found!",
                ex.getType(),
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles if the format of the user's birthdate
     * is invalid or not yet supported.
     *
     * @param ex the exception caused by incorrectly formatting the birthdate.
     * @return a readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleInvalidDateFormatException(
            final DateTimeParseException ex
    ) {
        log.error("Invalid date format!");
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.BAD_REQUEST,
                "Invalid date format! Allowed format is: YYYY-MM-DD",
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }


    /**
     * Handles the exception if the inputted gender is invalid.
     *
     * @param ex the exception caused by the invalid gender.
     * @return a readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidGenderException(
            final HttpMessageNotReadableException ex
    ) {
        String message = "Invalid gender! Allowed gender are: MALE or FEMALE";
        log.error(message);
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.BAD_REQUEST,
                message,
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles the exception if the ID document is invalid.
     *
     * @param ex the exception caused by the invalid ID document.
     * @return a readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(InvalidIdDocumentException.class)
    public ResponseEntity<Object> handleInvalidIdDocumentException(
            final InvalidIdDocumentException ex
    ) {
        log.error(ex.getMessage());
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles the exception if the users data are invalid.
     *
     * @param ex the exception caused by the invalid field.
     * @return a readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<Object> handleInvalidUserException(
            final InvalidUserException ex
    ) {
        log.error(ex.getMessage());
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Extracts the exceptions stacktrace into a
     * more readable {@code String} format.
     *
     * @param throwable the throwable object.
     * @return the chained {@code String} representation of the stacktrace.
     */
    private String getStackTraceAsString(final Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
