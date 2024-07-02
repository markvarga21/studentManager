package com.markvarga21.studentmanager.exception;

import com.markvarga21.studentmanager.util.Generated;
import lombok.Data;

/**
 * A custom exception for when the face data is invalid.
 */
@Data
@Generated
public class InvalidFacesException extends RuntimeException {
    /**
     * The percentage of the faces matching.
     */
    private Double percentage;
    /**
     * The default constructor.
     *
     * @param message The message to be sent when the exception occurs.
     * @param percentage The percentage of the faces matching.
     */
    public InvalidFacesException(
            final String message,
            final Double percentage
    ) {
        super(message);
        this.percentage = percentage;
    }
}
