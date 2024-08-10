package com.markvarga21.studentmanager.entity;

import com.markvarga21.studentmanager.util.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a student entity in the application.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Generated
public class Student {
    /**
     * A unique identifier for the student.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
     * The country of citizenship of the student.
     */
    private String countryOfCitizenship;

    /**
     * The student's gender.
     */
    private Gender gender;

    /**
     * The student's passport number.
     */
    @Column(unique = true, nullable = false)
    private String passportNumber;

    /**
     * The student's passports date of expiry.
     */
    private LocalDate passportDateOfExpiry;

    /**
     * The student's passports date of issue.
     */
    private LocalDate passportDateOfIssue;

    /**
     * The validity of the student's data,
     * which can be set by either an admin
     * or by an automatic checking procedure.
     */
    private boolean valid;
}
