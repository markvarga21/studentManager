package com.markvarga21.usermanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an address entity in the application.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    /**
     * A unique identifier for the address.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The country of the address.
     */
    private String country;

    /**
     * The city of the address.
     */
    private String city;

    /**
     * The street of the address.
     */
    private String street;

    /**
     * The street number of the address.
     */
    private Integer number;
}
