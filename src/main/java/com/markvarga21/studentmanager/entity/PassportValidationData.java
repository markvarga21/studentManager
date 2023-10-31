package com.markvarga21.studentmanager.entity;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.repository.PassportValidationDataRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    /**
     * Creates a new passport {@code StudentDto} object from
     * the provided {@code PassportValidationData}.
     *
     * @param passportValidationData The passport validation data.
     * @return A new {@code StudentDto} object.
     */
    public static StudentDto getStudentDtoFromValidationData(
            final PassportValidationData passportValidationData
    ) {
        return StudentDto.builder()
                .passportNumber(passportValidationData.passportNumber)
                .firstName(passportValidationData.firstName)
                .lastName(passportValidationData.lastName)
                .gender(passportValidationData.gender)
                .passportDateOfIssue(passportValidationData.passportDateOfIssue)
                .passportDateOfExpiry(passportValidationData.passportDateOfExpiry)
                .birthDate(passportValidationData.birthDate)
                .placeOfBirth(passportValidationData.placeOfBirth)
                .countryOfCitizenship(passportValidationData.countryOfCitizenship)
                .build();
    }
}
