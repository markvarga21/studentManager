package com.markvarga21.studentmanager.entity;

import com.markvarga21.studentmanager.util.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A class for storing the facial validation data.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class FacialValidationData {
    /**
     * The id of the facial validation data.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The passport number of the student.
     */
    private String passportNumber;

    /**
     * The validity of the facial validation data.
     */
    private Boolean isValid;

    /**
     * The percentage of the faces matching.
     */
    private Double percentage;
}
