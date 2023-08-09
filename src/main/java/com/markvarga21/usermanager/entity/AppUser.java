package com.markvarga21.usermanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    @OneToOne(cascade = CascadeType.ALL)
    private Address placeOfBirth;
    private String nationality;
    private Gender gender;
    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;
    private String email;
    private String phoneNumber;
}
