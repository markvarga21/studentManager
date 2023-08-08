package com.markvarga21.usermanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Long id;
    private String country;
    private String street;
    private Integer number;
}
