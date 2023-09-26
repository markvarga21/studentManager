package com.markvarga21.usermanager.dto;

import com.markvarga21.usermanager.entity.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a user data transfer object in the application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUserDto {
    /**
     * A unique identifier for the user.
     */
    private Long id;

    /**
     * The first name of the user.
     */
    @NotBlank(message = "First name cannot be null or empty!")
    private String firstName;

    /**
     * The last name of the user.
     */
    @NotBlank(message = "Last name cannot be null or empty!")
    private String lastName;

    /**
     * The birthdate name of the user.
     */
    @NotNull(message = "Date of birth cannot be null!")
    @PastOrPresent
    private LocalDate birthDate;

    /**
     * The birthplace of the user.
     */
    @Valid
    @NotNull(message = "Place of birth cannot be null!")
    private AddressDto placeOfBirth;

    /**
     * The nationality of the user.
     */
    @NotBlank(message = "Nationality cannot be null or empty!")
    private String countryOfCitizenship;

    /**
     * The user's gender.
     */
    @NotNull(message = "Gender cannot be null!")
    private Gender gender;

    /**
     * The user's email address.
     */
    @Email(message = "Email should be valid!")
    @NotBlank(message = "Email cannot be empty!")
    private String email;

    /**
     * The user's phone number.
     */
    @Pattern(regexp = "(^[0-9]+$)", message = "Invalid phone number!")
    @NotBlank(message = "Phone number cannot be empty!")
    private String phoneNumber;
}
