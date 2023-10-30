package com.markvarga21.studentmanager.entity;

import com.markvarga21.studentmanager.dto.StudentDto;
import jakarta.persistence.*;
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
     * The time of the validation.
     */
    private LocalDateTime timestamp;

    /**
     * The first name of the student.
     */
    private String firstName;

    /**
     * The last name of the student.
     */
    private String lastName;

    /**
     * The birthdate of the student.
     */
    private LocalDate birthDate;

    /**
     * The birthplace of the student.
     */
    private String placeOfBirth;

    /**
     * The nationality of the student.
     */
    private String countryOfCitizenship;

    /**
     * The student's gender.
     */
    private Gender gender;

    /**
     * The student's passport number.
     */
    @Id
    private String passportNumber;

    /**
     * The student's passports expiry date.
     */
    private LocalDate passportDateOfExpiry;

    /**
     * The student's passports issue date.
     */
    private LocalDate passportDateOfIssue;
}
