package com.markvarga21.studentmanager.exception.handler;

import com.markvarga21.studentmanager.exception.ApiError;
import com.markvarga21.studentmanager.exception.InvalidDateFormatException;
import com.markvarga21.studentmanager.exception.InvalidPassportException;
import com.markvarga21.studentmanager.exception.InvalidStudentException;
import com.markvarga21.studentmanager.exception.OperationType;
import com.markvarga21.studentmanager.exception.StudentNotFoundException;
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
     * A bullet point character in unicode.
     */
    public static final String POINT_UNICODE = "\u2022";

    /**
     * Handles if the user did not input the information correctly when using
     * the application's endpoints.
     *
     * @param ex The exception caused by the invalid field value.
     * @return A readable {@code ResponseEntity} containing useful information.
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
                "Invalid field in creating the user!\nViolations:\n%s",
                this.formatInvalidFieldsMap(errors)
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
     * Formats the invalid fields map into a more readable format.
     *
     * @param map The map containing the invalid fields.
     * @return The formatted {@code String}.
     */
    private String formatInvalidFieldsMap(final Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 0;
        for (var entry : map.entrySet()) {
            String value = POINT_UNICODE + " " + entry.getValue();
            stringBuilder.append(value);
            if (counter < map.size() - 1) {
                stringBuilder.append("\n");
            }
            counter++;
        }
        return stringBuilder.toString();
    }

    /**
     * Handles if the input fields are invalid.
     *
     * @param ex The exception caused by a constraint violation.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            final ConstraintViolationException ex
    ) {
        String message = String.format(
                "Invalid field in creating the student! Violations: %s",
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
     * Handles the exception if a student cannot be found in the database.
     *
     * @param ex The exception caused by not founding the student.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Object> handleStudentNotFoundException(
            final StudentNotFoundException ex
    ) {
        log.error("Student not found!");
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.NOT_FOUND,
                "Student not found!",
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
     * Handles if the format of the student's birthdate
     * is invalid or not yet supported.
     *
     * @param ex The exception caused by incorrectly formatting the birthdate.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler({
            DateTimeParseException.class,
            InvalidDateFormatException.class
    })
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
     * @param ex The exception caused by the invalid gender.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidGenderException(
            final HttpMessageNotReadableException ex
    ) {
        String message = "Invalid gender! Allowed genders are: MALE or FEMALE";
        log.error(message);
        String stackTraceAsString = getStackTraceAsString(ex);
        if (stackTraceAsString.contains("LocalDate")) {
            String invalidDateMessage = "Invalid date format! Allowed format is: YYYY-MM-DD";
            ApiError apiError = new ApiError(
                    new Date(),
                    HttpStatus.BAD_REQUEST,
                    invalidDateMessage,
                    OperationType.CREATE,
                    stackTraceAsString
            );
            return new ResponseEntity<>(
                    apiError,
                    new HttpHeaders(),
                    apiError.getStatus()
            );
        } else {
            ApiError apiError = new ApiError(
                    new Date(),
                    HttpStatus.BAD_REQUEST,
                    message,
                    OperationType.CREATE,
                    stackTraceAsString
            );
            return new ResponseEntity<>(
                    apiError,
                    new HttpHeaders(),
                    apiError.getStatus()
            );
        }

    }

    /**
     * Handles the exception if the passport is invalid.
     *
     * @param ex The exception caused by the invalid passport.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(InvalidPassportException.class)
    public ResponseEntity<Object> handleInvalidPassportException(
            final InvalidPassportException ex
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
     * Handles the exception if the student's data are invalid.
     *
     * @param ex The exception caused by the invalid field.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(InvalidStudentException.class)
    public ResponseEntity<Object> handleInvalidStudentException(
            final InvalidStudentException ex
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
     * @param throwable The throwable object.
     * @return The chained {@code String} representation of the stacktrace.
     */
    private String getStackTraceAsString(final Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
