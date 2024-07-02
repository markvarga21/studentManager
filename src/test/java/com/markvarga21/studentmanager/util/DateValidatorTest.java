package com.markvarga21.studentmanager.util;

import com.markvarga21.studentmanager.exception.InvalidDateException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateValidatorTest {
    @Test
    void shouldValidateBirthDateWhenValid() {
        // Given
        LocalDate validBirthDate = LocalDate.of(1990, 1, 1);

        // When
        DateValidator.validateBirthdate(validBirthDate);

        // Then
        assertDoesNotThrow(() -> DateValidator.validateBirthdate(validBirthDate));
    }

    @Test
    void shouldValidateBirthDateWhenInvalid() {
        // Given
        LocalDate invalidBirthDate = LocalDate
                .now()
                .plusDays(1);

        // When
        // Then
        assertThrows(InvalidDateException.class, () -> DateValidator.validateBirthdate(invalidBirthDate));
    }

    @Test
    void shouldValidatePassportIssueDateWhenValid() {
        // Given
        LocalDate validPassportIssueDate = LocalDate.of(2020, 1, 1);

        // When
        DateValidator.validatePassportIssueDate(validPassportIssueDate);

        // Then
        assertDoesNotThrow(() -> DateValidator.validatePassportIssueDate(validPassportIssueDate));
    }

    @Test
    void shouldValidatePassportIssueDateWhenInvalid() {
        // Given
        LocalDate invalidPassportIssueDate = LocalDate
                .now()
                .plusDays(1);

        // When
        // Then
        assertThrows(InvalidDateException.class, () -> DateValidator.validatePassportIssueDate(invalidPassportIssueDate));
    }

    @Test
    void shouldValidatePassportExpiryDateWhenValid() {
        // Given
        LocalDate validPassportExpiryDate = LocalDate
                .now()
                .plusDays(1);

        // When
        DateValidator.validatePassportExpiryDate(validPassportExpiryDate);

        // Then
        assertDoesNotThrow(() -> DateValidator.validatePassportExpiryDate(validPassportExpiryDate));
    }

    @Test
    void shouldValidatePassportExpiryDateWhenInvalid() {
        // Given
        LocalDate invalidPassportExpiryDate = LocalDate.of(2020, 1, 1);

        // When
        // Then
        assertThrows(InvalidDateException.class, () -> DateValidator.validatePassportExpiryDate(invalidPassportExpiryDate));
    }
}