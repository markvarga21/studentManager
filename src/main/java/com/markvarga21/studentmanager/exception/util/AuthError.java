package com.markvarga21.studentmanager.exception.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A util class which contains information about an
 * access-denied authorization problem.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthError {
    /**
     * The error itself.
     */
    private String error;

    /**
     * The message of the error.
     */
    private String message;

    /**
     * The status code of the error.
     */
    private int status;
}
