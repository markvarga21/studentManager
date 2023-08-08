package com.markvarga21.usermanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    @NotBlank(message = "First name cannot be null or empty!")
    private String firstName;

    @NotBlank(message = "Last name cannot be null or empty!")
    private String lastName;

    @NotNull(message = "Date of birth cannot be null!")
    @PastOrPresent
    private LocalDate birthDate;

    @OneToOne
    @NotNull(message = "Place of birth cannot be null!")
    private Address placeOfBirth;

    @NotBlank(message = "Nationality cannot be null or empty!")
    private String nationality;

    @NotNull(message = "Gender cannot be null!")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @ManyToOne
    @NotNull(message = "Address cannot be null!")
    private Address address;

    @Email(message = "Email should be valid!")
    @NotBlank
    private String email;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Invalid phone number!")
    @NotBlank(message = "Phone number cannot be empty!")
    private String phoneNumber;
}
