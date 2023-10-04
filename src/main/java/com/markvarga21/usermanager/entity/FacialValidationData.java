package com.markvarga21.usermanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An entity which stores the facial validation data.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacialValidationData {
    /**
     * The ID of the facial validation data.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * The validity of the faces.
     */
    private Double probabilityOfMatching;

}
