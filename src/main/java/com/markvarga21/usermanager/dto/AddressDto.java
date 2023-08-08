package com.markvarga21.usermanager.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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

    @Positive(message = "Number has to be positive!")
    private Integer number;
}
