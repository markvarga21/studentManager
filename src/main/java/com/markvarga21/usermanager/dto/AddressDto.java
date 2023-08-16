package com.markvarga21.usermanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an address data transfer object in the application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    /**
     * A unique identifier for the address.
     */
    private Long id;

    /**
     * The country of the address.
     */
    @NotBlank(message = "Country cannot be empty!")
    private String country;

    /**
     * The city of the address.
     */
    @NotBlank(message = "Country cannot be empty!")
    private String city;

    /**
     * The street of the address.
     */
    @NotBlank(message = "Street cannot be empty!")
    private String street;

    /**
     * The street number of the address.
     */
    @Min(value = 1, message = "Street number has to be larger than 1!")
    private Integer number;
}
