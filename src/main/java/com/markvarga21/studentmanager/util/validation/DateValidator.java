package com.markvarga21.studentmanager.util.validation;

import com.markvarga21.studentmanager.exception.InvalidDateException;

import java.time.LocalDate;

/**
 * A utility class which is used to validate dates.
 */
public final class DateValidator {
    private DateValidator() {

    }

    /**
     * Validates the birthdate.
     *
     * @param birthdate The birthdate to be validated.
     */
    public static void validateBirthdate(final LocalDate birthdate) {
        if (birthdate.isAfter(LocalDate.now())) {
            throw new InvalidDateException("Birthdate cannot be in the future!");
        }
    }

    /**
     * Validates the passport date of expiry.
     *
     * @param date The passport date of expiry to be validated.
     */
    public static void validatePassportIssueDate(final LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new InvalidDateException("Passport date of issue cannot be in the future!");
        }
    }

    /**
     * Validates the passport date of expiry.
     *
     * @param date The passport date of expiry to be validated.
     */
    public static void validatePassportExpiryDate(final LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidDateException("Passport date of expiry cannot be in the past!");
        }
    }
}
