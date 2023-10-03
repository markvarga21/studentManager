package com.markvarga21.usermanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * An entity class which is used to store the data extracted
 * from the passport.
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PassportValidationData {
    /**
     * The ID of the passport validation entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The time of the validation.
     */
    private LocalDateTime timestamp;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * The birthdate of the user.
     */
    private LocalDate birthDate;

    /**
     * The birthplace of the user.
     */
    private String placeOfBirth;

    /**
     * The nationality of the user.
     */
    private String countryOfCitizenship;

    /**
     * The user's gender.
     */
    private Gender gender;

    /**
     * The user's passport number.
     */
    private String passportNumber;

    /**
     * The user's passports expiry date.
     */
    private LocalDate passportDateOfExpiry;

    /**
     * The user's passports issue date.
     */
    private LocalDate passportDateOfIssue;
}
