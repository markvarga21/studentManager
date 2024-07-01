package com.markvarga21.studentmanager.service.validation.face.impl;

import com.markvarga21.studentmanager.entity.FacialValidationData;
import com.markvarga21.studentmanager.exception.FaceValidationDataNotFoundException;
import com.markvarga21.studentmanager.repository.FacialValidationDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FacialValidationServiceImplTest {
    /**
     * Facial validation mock service under test.
     */
    @InjectMocks
    private FacialValidationServiceImpl facialValidationService;

    /**
     * The repository for facial validation data.
     */

    @Spy
    private FacialValidationDataRepository repository;

    /**
     * A static passport number for testing purposes.
     */
    static final String PASSPORT_NUMBER = "123456";

    /**
     * A valid static mock facial validation data.
     */
    static final FacialValidationData VALID_FACIAL_VALIDATION_DATA = FacialValidationData.builder()
            .passportNumber(PASSPORT_NUMBER)
            .isValid(true)
            .percentage(0.9)
            .build();

    /**
     * An invalid static mock facial validation data.
     */
    static final FacialValidationData INVALID_FACIAL_VALIDATION_DATA = FacialValidationData.builder()
            .passportNumber(PASSPORT_NUMBER)
            .isValid(false)
            .percentage(0.2)
            .build();


    @Test
    void shouldSaveFacialValidationDataIfPresentTest() {
        // Given
        // When
        when(this.repository.getFacialValidationDataByPassportNumber(PASSPORT_NUMBER))
                .thenReturn(Optional.of(VALID_FACIAL_VALIDATION_DATA));
        this.facialValidationService.saveFacialValidationData(VALID_FACIAL_VALIDATION_DATA);

        // Then
        verify(this.repository, times(1))
                .deleteFacialValidationDataByPassportNumber(PASSPORT_NUMBER);
        verify(this.repository, times(1))
                .save(VALID_FACIAL_VALIDATION_DATA);
    }

    @Test
    void shouldSaveFacialValidationDataIfNotPresentTest() {
        // Given
        // When
        when(this.repository.getFacialValidationDataByPassportNumber(PASSPORT_NUMBER))
                .thenReturn(Optional.empty());
        this.facialValidationService.saveFacialValidationData(VALID_FACIAL_VALIDATION_DATA);

        // Then
        verify(this.repository, times(0))
                .deleteFacialValidationDataByPassportNumber(PASSPORT_NUMBER);
        verify(this.repository, times(1))
                .save(VALID_FACIAL_VALIDATION_DATA);
    }

    @Test
    void shouldGetFacialValidationDataByPassportIfPresentTest() {
        // Given
        // When
        when(this.repository.getFacialValidationDataByPassportNumber(PASSPORT_NUMBER))
                .thenReturn(Optional.of(VALID_FACIAL_VALIDATION_DATA));
        FacialValidationData actual = this.facialValidationService
                .getFacialValidationDataByPassportNumber(PASSPORT_NUMBER);

        // Then
        assertNotNull(actual);
        assertEquals(VALID_FACIAL_VALIDATION_DATA, actual);
    }

    @Test
    void shouldNotGetFacialValidationDataByPassportIfNotPresentTest() {
        // Given
        // When
        when(this.repository.getFacialValidationDataByPassportNumber(PASSPORT_NUMBER))
                .thenReturn(Optional.empty());
        FacialValidationData actual = this.facialValidationService
                .getFacialValidationDataByPassportNumber(PASSPORT_NUMBER);

        // Then
        assertNull(actual);
    }

    @Test
    void shouldFetchAllFacialValidationDataTest() {
        // Given
        // When
        when(this.repository.findAll())
                .thenReturn(List.of(VALID_FACIAL_VALIDATION_DATA, INVALID_FACIAL_VALIDATION_DATA));
        List<FacialValidationData> actual = this.facialValidationService.getAllFacialValidationData();

        // Then
        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertTrue(actual.contains(VALID_FACIAL_VALIDATION_DATA));
        assertTrue(actual.contains(INVALID_FACIAL_VALIDATION_DATA));
    }

    @Test
    void shouldDeleteFacialValidationDataIfPresentTest() {
        // Given
        String expected = String
                .format("Facial validation data for passport number '%s' deleted successfully!", PASSPORT_NUMBER);

        // When
        when(this.repository.getFacialValidationDataByPassportNumber(PASSPORT_NUMBER))
                .thenReturn(Optional.of(VALID_FACIAL_VALIDATION_DATA));
        String actual = this.facialValidationService
                .deleteFacialValidationDataByPassportNumber(PASSPORT_NUMBER);

        // Then
        verify(this.repository, times(1))
                .deleteFacialValidationDataByPassportNumber(PASSPORT_NUMBER);
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionUponPassportValidationDataIfNotPresentTest() {
        // Given
        // When
        when(this.repository.getFacialValidationDataByPassportNumber(PASSPORT_NUMBER))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                FaceValidationDataNotFoundException.class,
                () -> this.facialValidationService.deleteFacialValidationDataByPassportNumber(PASSPORT_NUMBER)
        );
    }

    @Test
    void shouldSetFacialValidationToValidIfPresent() {
        // Given
        FacialValidationData data = INVALID_FACIAL_VALIDATION_DATA;
        String expected = String
                .format("Facial validation data for passport number '%s' set to valid!", PASSPORT_NUMBER);

        // When
        when(this.repository.getFacialValidationDataByPassportNumber(PASSPORT_NUMBER))
                .thenReturn(Optional.of(data));
        String actual = this.facialValidationService
                .setFacialValidationToValid(PASSPORT_NUMBER);

        // Then
        verify(this.repository, times(1))
                .save(INVALID_FACIAL_VALIDATION_DATA);
        assertEquals(expected, actual);
        assertTrue(data.getIsValid());
        assertEquals(1.0, data.getPercentage());
    }

    @Test
    void shouldSetFacialValidationToValidIfNotPresent() {
        // Given
        // When
        when(this.repository.getFacialValidationDataByPassportNumber(PASSPORT_NUMBER))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                FaceValidationDataNotFoundException.class,
                () -> this.facialValidationService.setFacialValidationToValid(PASSPORT_NUMBER)
        );
    }
}