package com.markvarga21.usermanager.dto;

import com.markvarga21.usermanager.entity.Address;
import com.markvarga21.usermanager.entity.Gender;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto {
    private Long Id;
    @NotBlank(message = "First name cannot be null or empty!")
    private String firstName;

    @NotBlank(message = "Last name cannot be null or empty!")
    private String lastName;

    @NotNull(message = "Date of birth cannot be null!")
    @PastOrPresent
    private LocalDate birthDate;

    @Valid
    @NotNull(message = "Place of birth cannot be null!")
    private AddressDto placeOfBirth;

    @NotBlank(message = "Nationality cannot be null or empty!")
    private String nationality;

    @NotNull(message = "Gender cannot be null!")
    private Gender gender;

    @Valid
    @NotNull(message = "Address cannot be null!")
    private AddressDto address;

    @Email(message = "Email should be valid!")
    @NotBlank(message = "Email cannot be empty!")
    private String email;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Invalid phone number!")
    @NotBlank(message = "Phone number cannot be empty!")
    private String phoneNumber;
}
