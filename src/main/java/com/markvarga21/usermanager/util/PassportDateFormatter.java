package com.markvarga21.usermanager.util;

import com.markvarga21.usermanager.exception.InvalidDateFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Component
@Slf4j
public class PassportDateFormatter {
        public LocalDate format(final String passportDate) {
            String[] birthDateComponents = passportDate.split(" ");
            String formattedDate = String.format(
                    "%s %s %s",
                    birthDateComponents[0],
                    birthDateComponents[1].split("/")[1],
                    birthDateComponents[2]
            );

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);

            try {
                LocalDate date = formatter.parse(formattedDate).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                log.info("Date is {}", date);
                return date;
            } catch (Exception e) {
                log.error("Invalid date format: {}", formattedDate);
                throw new InvalidDateFormatException(formattedDate);
            }
        }

        public String convertLocalDateToString(final LocalDate date) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return date.format(formatter);
        }
}
