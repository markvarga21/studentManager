package com.markvarga21.studentmanager.exception.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A custom class for sending back custom API
 * error when faces are not identical.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvalidFacesApiError {
    /**
     * The message of the error.
     */
    private String message;

    /**
     * The percentage of the faces matching.
     */
    private Double percentage;
}
