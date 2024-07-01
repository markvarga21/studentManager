package com.markvarga21.studentmanager.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CountryNameFetcherTest {
    /**
     * The country name fetcher under test.
     */
    @Autowired
    private CountryNameFetcher countryNameFetcher;

    @Test
    void shouldFetchCountryForCodeTest() {
        // Given
        final String countryCode = "HUN";

        // When
        final String countryName = countryNameFetcher
                .getCountryNameForCode(countryCode);

        // Then
        assertEquals("Hungary", countryName);
    }
}