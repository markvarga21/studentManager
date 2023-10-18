package com.markvarga21.studentmanager.dto;

import com.markvarga21.studentmanager.entity.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a student data transfer object in the application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDto {
    /**
     * A unique identifier for the student.
     */
    private Long id;

    /**
     * The first name of the student.
     */
    @NotBlank(message = "First name cannot be null or empty!")
    private String firstName;

    /**
     * The last name of the student.
     */
    @NotBlank(message = "Last name cannot be null or empty!")
    private String lastName;

    /**
     * The birthdate name of the student.
     */
    @NotNull(message = "Date of birth cannot be null!")
    @PastOrPresent
    private LocalDate birthDate;

    /**
     * The birthplace of the student.
     */
    @Valid
    @NotNull(message = "Place of birth cannot be null!")
    private String placeOfBirth;

    /**
     * The nationality of the student.
     */
    @NotBlank(message = "Nationality cannot be null or empty!")
    private String countryOfCitizenship;

    /**
     * The student's gender.
     */
    @NotNull(message = "Gender cannot be null!")
    private Gender gender;

    /**
     * The student's passport number.
     */
    @NotBlank(message = "Passport number cannot be empty!")
    private String passportNumber;

    /**
     * The student's passports expiry date.
     */
    @NotNull(message = "Passport date of expiry cannot be null!")
    @FutureOrPresent
    private LocalDate passportDateOfExpiry;

    /**
     * The student's passports issue date.
     */
    @NotNull(message = "Passport date of issue cannot be null!")
    @PastOrPresent
    private LocalDate passportDateOfIssue;
}
