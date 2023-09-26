package com.markvarga21.usermanager.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a user entity in the application.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser {
    /**
     * A unique identifier for the user.
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
     * The birthdate of the user.
     */
    private LocalDate birthDate;

    /**
     * The birthplace of the user.
     */
    @OneToOne(cascade = CascadeType.ALL)
    private Address placeOfBirth;

    /**
     * The nationality of the user.
     */
    private String countryOfCitizenship;

    /**
     * The user's gender.
     */
    private Gender gender;

    /**
     * The user's email address.
     */
    private String email;

    /**
     * The user's phone number.
     */
    private String phoneNumber;

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
