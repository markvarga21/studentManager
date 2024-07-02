package com.markvarga21.studentmanager.util;

import com.markvarga21.studentmanager.exception.InvalidDateFormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateDeserializerTest {
    @ParameterizedTest
    @CsvSource({
            "2001/02/03",
            "2001-02-03",
            "2001.02.03",
            "03/02/2001",
            "03-02-2001",
            "03.02.2001",
            "03/02/01",
            "03-02-01",
            "03.02.01",
            "03 FEB 01",
            "03 FEB 2001",
    })
    void shouldConvertStringToLocalDateWhenHasSupportedFormatTest(final String input) {
        // Given
        LocalDate expected = LocalDate.of(2001, 2, 3);

        // When
        LocalDate actual = DateDeserializer.mapDateStringToLocalDate(input);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionWhenInvalidDateTest() {
        // Given
        String invalidDate = "2001/FEBRUARY/03";

        // When
        // Then
        assertThrows(
                InvalidDateFormatException.class,
                () -> DateDeserializer.mapDateStringToLocalDate(invalidDate)
        );
    }

    @Test
    void shouldMapPassportDateToLocalDateTest() {
        // Given
        String passportDate = "03 FEB 01";
        LocalDate expected = LocalDate.of(2001, 2, 3);

        // When
        LocalDate actual = DateDeserializer.mapDateStringToLocalDate(passportDate);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void shouldMapLocalDateToStringTest() {
        // Given
        LocalDate date = LocalDate.of(2001, 2, 3);
        String expected = "2001-02-03";

        // When
        String actual = DateDeserializer.mapLocalDateToDateString(date);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void shouldMapPassportDateTest() {
        // Given
        String date = "08 IAN/JAN 19";

        // When
        LocalDate actual = DateDeserializer.mapDateStringToLocalDate(date);

        // Then
        assertEquals(LocalDate.of(2019, 1, 8), actual);
    }
}