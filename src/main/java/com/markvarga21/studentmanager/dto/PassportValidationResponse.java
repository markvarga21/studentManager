package com.markvarga21.studentmanager.dto;

import com.markvarga21.studentmanager.util.Generated;
import lombok.Builder;
import lombok.Data;

/**
 * A DTO class which is used when sending back passport
 * validation information.
 */
@Data
@Builder
@Generated
public class PassportValidationResponse {
    /**
     * The validity of the passport and the
     * data entered by the user.
     */
    private Boolean isValid;

    /**
     * The data extracted from the passport, if
     * the entered data is invalid, else it returns
     * an empty or null object.
     */
    private StudentDto studentDto;
}
