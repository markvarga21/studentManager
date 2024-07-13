package com.markvarga21.studentmanager.dto;

import com.markvarga21.studentmanager.entity.Gender;
import com.markvarga21.studentmanager.util.Generated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represents a student data transfer object in the application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
@Generated
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
    private String birthDate;

    /**
     * The birthplace of the student.
     */
    @NotNull(message = "Place of birth cannot be null!")
    private String placeOfBirth;

    /**
     * The nationality of the student.
     */
    @NotBlank(message = "Country of citizenship cannot be null or empty!")
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
    @SuppressWarnings("checkstyle:LineLength")
    @NotNull(message = "Passport date of expiry cannot be null and has to be either in the present or in the future!")
    private String passportDateOfExpiry;

    /**
     * The student's passports issue date.
     */
    @SuppressWarnings("checkstyle:LineLength")
    @NotNull(message = "Passport date of issue cannot be null and has to be either in the past or in the present!")
    private String passportDateOfIssue;

    /**
     * The validity of the student's data
     * which can be set by either an admin
     * or by an automatic checking procedure.
     */
    private boolean valid;

    /**
     * The initial non-zero odd number for the hash code.
     */
    private static final int INITIAL_NON_ZERO_ODD_NUMBER = 17;

    /**
     * The multiplier non-zero odd number for the hash code.
     */
    private static final int MULTIPLIER_NON_ZERO_ODD_NUMBER = 31;

//    /**
//     * Clone a student.
//     *
//     * @return A clone of the student.
//     */
//    public StudentDto clone() {
//        return StudentDto.builder()
//                .id(this.id)
//                .firstName(this.firstName)
//                .lastName(this.lastName)
//                .birthDate(this.birthDate)
//                .placeOfBirth(this.placeOfBirth)
//                .countryOfCitizenship(this.countryOfCitizenship)
//                .gender(this.gender)
//                .passportNumber(this.passportNumber)
//                .passportDateOfIssue(this.passportDateOfIssue)
//                .passportDateOfExpiry(this.passportDateOfExpiry)
//                .build();
//    }

    /**
     * Checks if two students are equal.
     *
     * @param o The other student.
     * @return {@code true} if the students are equal,
     * {@code false} otherwise.
     */
    @SuppressWarnings("checkstyle:LineLength")
    public boolean equals(final Object o) {
        if (!(o instanceof StudentDto student)) {
            return false;
        }
        if (this.firstName == null || student.getFirstName() == null) {
            return false;
        }
        if (this.lastName == null || student.getLastName() == null) {
            return false;
        }
        if (this.birthDate == null || student.getBirthDate() == null) {
            return false;
        }
        if (this.placeOfBirth == null || student.getPlaceOfBirth() == null) {
            return false;
        }
        if (this.countryOfCitizenship == null || student.getCountryOfCitizenship() == null) {
            return false;
        }
        if (this.gender == null || student.getGender() == null) {
            return false;
        }
        if (this.passportNumber == null || student.getPassportNumber() == null) {
            return false;
        }
        if (this.passportDateOfIssue == null || student.getPassportDateOfIssue() == null) {
            return false;
        }
        if (this.passportDateOfExpiry == null || student.getPassportDateOfExpiry() == null) {
            return false;
        }

        return student.getFirstName()
                    .equalsIgnoreCase(this.firstName)
                && student.getLastName()
                    .equalsIgnoreCase(this.lastName)
                && student.getBirthDate()
                    .equals(this.birthDate)
                && student.getPlaceOfBirth()
                    .equalsIgnoreCase(this.placeOfBirth)
                && student.getCountryOfCitizenship()
                    .equalsIgnoreCase(this.countryOfCitizenship)
                && student.getGender()
                    .equals(this.gender)
                && student.getPassportNumber()
                    .equalsIgnoreCase(this.passportNumber)
                && student.getPassportDateOfIssue()
                    .equals(this.passportDateOfIssue)
                && student.getPassportDateOfExpiry()
                    .equals(this.passportDateOfExpiry);
    }

    /**
     * Generates a hash code for the student.
     *
     * @return The hash code.
     */
    @SuppressWarnings("checkstyle:LineLength")
    public int hashCode() {
        return new HashCodeBuilder(INITIAL_NON_ZERO_ODD_NUMBER, MULTIPLIER_NON_ZERO_ODD_NUMBER)
                .append(this.id)
                .append(this.firstName)
                .append(this.lastName)
                .append(this.birthDate)
                .append(this.placeOfBirth)
                .append(this.countryOfCitizenship)
                .append(this.gender)
                .append(this.passportNumber)
                .append(this.passportDateOfIssue)
                .append(this.passportDateOfExpiry)
                .toHashCode();
    }
}
