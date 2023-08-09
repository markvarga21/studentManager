package com.markvarga21.usermanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
public class AppUser {
    /**
     * A unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * The birthdate name of the user.
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
    private String nationality;

    /**
     * The user's gender.
     */
    private Gender gender;

    /**
     * The user's address.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    /**
     * The user's email address.
     */
    private String email;

    /**
     * The user's phone number.
     */
    private String phoneNumber;
}
