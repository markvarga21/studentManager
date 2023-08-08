package com.markvarga21.usermanager.dto;

import com.markvarga21.usermanager.entity.Address;
import com.markvarga21.usermanager.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto {
    private Long Id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Address placeOfBirth;
    private String nationality;
    private Gender gender;
    private Address address;
    private String email;
    private String phoneNumber;
}
