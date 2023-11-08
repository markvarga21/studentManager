package com.markvarga21.studentmanager.util;

import com.markvarga21.studentmanager.exception.InvalidDateFormatException;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.time.ZoneId.systemDefault;

/**
 * A utility class which is used to deserialize
 * {@code String} representation of a date.
 */
@Slf4j
public final class DateDeserializer {
    /**
     * The length of the standard passport date components.
     */
    public static final int PASSPORT_DATE_COMPONENT_LENGTH = 3;

    /**
     * The predefined date formats.
     */
    public static final String[] DATE_FORMATS = {
            "yyyy/MM/dd",
            "yyyy-MM-dd",
            "yyyy.MM.dd",
            "dd/MM/yyyy",
            "dd-MM-yyyy",
            "dd.MM.yyyy",
            "MM/dd/yyyy",
            "MM-dd-yyyy",
            "MM.dd.yyyy",
            "dd/MM/yy",
            "dd-MM-yy",
            "dd.MM.yy",
            "MM/dd/yy",
            "MM-dd-yy",
            "MM.dd.yy",
            "yy/MM/dd",
            "yy-MM-dd",
            "yy.MM.dd",
            "dd MMM yy",
            "dd MMM yyyy"
    };
    private DateDeserializer() {

    }

    /**
     * Maps the {@code String} representation of a date
     * to a {@code LocalDate} object.
     *
     * @param dateString The {@code String} representation of a date.
     * @return The {@code LocalDate} object.
     */
    public static LocalDate mapDateStringToLocalDate(
            final String dateString
    ) {
        log.info("Date to deserialize: {}", dateString);
        if (dateString.split("/").length == 2 || dateString.split(" ").length == 3) {
            return mapStandardPassportDateStringToLocalDate(dateString);
        }
        for (String dateFormat : DATE_FORMATS) {
            try {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
                LocalDate passportDate = LocalDate.parse(dateString, dateTimeFormatter);
                log.info("Date from passport: {}", passportDate);
                return passportDate;
            } catch (Exception e) {
                // Ignore.
            }
        }
        throw new InvalidDateFormatException(dateString);
    }

    private static LocalDate mapStandardPassportDateStringToLocalDate(
            final String dateString
    ) {
        String formattedDate;
        if (dateString.contains("/")) {
            String[] birthDateComponents = dateString.split("/");
            String day = birthDateComponents[0].strip().charAt(0) + String.valueOf(birthDateComponents[0].strip().charAt(1));
            String month = birthDateComponents[1].strip().split(" ")[0].strip();
            String year = birthDateComponents[1].strip().split(" ")[1].strip();
            formattedDate = String.format("%s %s %s", day, month, year);
        } else {
            formattedDate = dateString;
        }

        log.info("Formatted standard passport date: {}", formattedDate);

        SimpleDateFormat[] formatters = {
                new SimpleDateFormat("dd MMM yy", Locale.ENGLISH),
                new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH),
        };

        for (SimpleDateFormat formatter : formatters) {
            try {
                return formatter
                        .parse(formattedDate)
                        .toInstant()
                        .atZone(systemDefault())
                        .toLocalDate();
            } catch (Exception e) {
                log.error("Invalid date format: {}", formattedDate);
            }
        }
        throw new InvalidDateFormatException(formattedDate);
    }

    /**
     * Maps a {@code LocalDate} object to a {@code String}.
     *
     * @param date The {@code LocalDate} object.
     * @return The {@code String} representation of the date.
     */
    public static String mapLocalDateToDateString(final LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
}
