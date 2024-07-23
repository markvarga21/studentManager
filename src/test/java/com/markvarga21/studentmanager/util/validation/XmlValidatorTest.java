package com.markvarga21.studentmanager.util.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class XmlValidatorTest {
    /**
     * The XML validator component under testing.
     */
    private XmlValidator xmlValidator;

    /**
     * The valid XML string for testing.
     */
    private static final String VALID_STUDENT = """
    <?xml version="1.0" encoding="UTF-8"?>
    <students>
        <student id="1">
            <name>
                <firstName>Emily</firstName>
                <lastName>Johnson</lastName>
            </name>
            <dateOfBirth>
                <year>1995</year>
                <month>02</month>
                <day>12</day>
            </dateOfBirth>
            <placeOfBirth>New York</placeOfBirth>
            <countryOfCitizenship>American</countryOfCitizenship>
            <gender>female</gender>
            <passportNumber>US123456</passportNumber>
            <dateOfIssue>
                <year>2015</year>
                <month>03</month>
                <day>15</day>
            </dateOfIssue>
            <dateOfExpiry>
                <year>2025</year>
                <month>03</month>
                <day>15</day>
            </dateOfExpiry>
            <status>valid</status>
        </student>
    </students>
    """;

    /**
     * The invalid XML string for testing.
     */
    private static final String INVALID_STUDENT = """
    <?xml version="1.0" encoding="UTF-8"?>
    <students>
        <student id="1">
            <dateOfBirth>
                <year>1995</year>
                <month>02</month>
                <day>12</day>
            </dateOfBirth>
            <placeOfBirth>New York</placeOfBirth>
            <countryOfCitizenship>American</countryOfCitizenship>
            <gender>female</gender>
            <passportNumber>US123456</passportNumber>
            <dateOfIssue>
                <year>2015</year>
                <month>03</month>
                <day>15</day>
            </dateOfIssue>
            <dateOfExpiry>
                <year>2025</year>
                <month>03</month>
                <day>15</day>
            </dateOfExpiry>
            <status>valid</status>
        </student>
    </students>
    """;

    @BeforeEach
    void setUp() {
        this.xmlValidator = new XmlValidator();
    }

    @Test
    void shouldReturnTrueWhenXmlIsValid() throws IOException {
        // Given
        // When
        boolean actual = this.xmlValidator
                .isXmlValid(VALID_STUDENT);

        // Then
        assertTrue(actual);
    }

    @Test
    void shouldReturnFalseWhenXmlIsInvalid() throws IOException {
        // Given
        // When
        boolean actual = this.xmlValidator
                .isXmlValid(INVALID_STUDENT);

        // Then
        assertFalse(actual);
    }
}