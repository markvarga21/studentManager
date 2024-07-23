package com.markvarga21.studentmanager.util.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.markvarga21.studentmanager.data.TestingData.INVALID_XML_STUDENT;
import static com.markvarga21.studentmanager.data.TestingData.VALID_XML_STUDENT;
import static org.junit.jupiter.api.Assertions.*;

class XmlValidatorTest {
    /**
     * The XML validator component under testing.
     */
    private XmlValidator xmlValidator;

    @BeforeEach
    void setUp() {
        this.xmlValidator = new XmlValidator();
    }

    @Test
    void shouldReturnTrueWhenXmlIsValid() throws IOException {
        // Given
        // When
        boolean actual = this.xmlValidator
                .isXmlValid(VALID_XML_STUDENT);

        // Then
        assertTrue(actual);
    }

    @Test
    void shouldReturnFalseWhenXmlIsInvalid() throws IOException {
        // Given
        // When
        boolean actual = this.xmlValidator
                .isXmlValid(INVALID_XML_STUDENT);

        // Then
        assertFalse(actual);
    }
}