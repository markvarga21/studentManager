package com.markvarga21.usermanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Long id;

    @NotBlank(message = "Country cannot be empty!")
    private String country;

    @NotBlank(message = "Street cannot be empty!")
    private String street;

    @Min(value = 1, message = "Street number has to be larger than 1!")
    private Integer number;
}
