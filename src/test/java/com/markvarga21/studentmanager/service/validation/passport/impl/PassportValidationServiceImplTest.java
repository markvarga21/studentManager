package com.markvarga21.studentmanager.service.validation.passport.impl;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.Gender;
import com.markvarga21.studentmanager.entity.PassportValidationData;
import com.markvarga21.studentmanager.exception.InvalidPassportException;
import com.markvarga21.studentmanager.exception.PassportValidationDataNotFoundException;
import com.markvarga21.studentmanager.repository.PassportValidationDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.markvarga21.studentmanager.data.TestingData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassportValidationServiceImplTest {
    /**
     * The passport validation service under testing.
     */
    @InjectMocks
    private PassportValidationServiceImpl service;

    /**
     * The repository for the passport validation data.
     */
    @Spy
    private PassportValidationDataRepository repository;

    @Test
    void shouldFetchAllPassportValidationDataTest() {
        // Given
        List<PassportValidationData> expected = List.of(VALIDATION_DATA);
        Page<PassportValidationData> page = new PageImpl<>(expected);

        // When
        when(repository.findAll(any(Pageable.class)))
                .thenReturn(page);
        List<PassportValidationData> actual = this.service
                .getAllPassportValidationData(PAGE, SIZE)
                .getContent();

        // Assert
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void shouldDeletePassportValidationDataIfPresentTest() {
        // Given
        String expected = String.format(
                "Passport validation data with passport number %s deleted.",
                VALIDATION_DATA.getPassportNumber()
        );

        // When
        when(this.repository
                .getPassportValidationDataByPassportNumber(VALIDATION_DATA.getPassportNumber()))
                .thenReturn(Optional.of(VALIDATION_DATA));
        String actual = this.service
                .deletePassportValidationData(VALIDATION_DATA.getPassportNumber());

        // Then
        verify(this.repository, times(1))
                .deletePassportValidationDataByPassportNumber(VALIDATION_DATA.getPassportNumber());
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionWhenValidationDataIsNotFoundTest() {
        // Given
        // When
        when(this.repository
                .getPassportValidationDataByPassportNumber(VALIDATION_DATA.getPassportNumber()))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                PassportValidationDataNotFoundException.class,
                () -> this.service.deletePassportValidationData(VALIDATION_DATA.getPassportNumber())
        );
    }

    @Test
    void shouldGetPassportValidationDataForPassportNumberTest() {
        // Given
        // When
        when(this.repository.getPassportValidationDataByPassportNumber(VALIDATION_DATA.getPassportNumber()))
                .thenReturn(Optional.of(VALIDATION_DATA));
        Optional<PassportValidationData> actual = this.service
                .getPassportValidationDataByPassportNumber(VALIDATION_DATA.getPassportNumber());

        // Then
        assertThat(actual).isPresent();
    }

    @Test
    void shouldCreatePassportValidationDataIfNotPresentTest() {
        // Given
        // When
        when(this.repository.getPassportValidationDataByPassportNumber(VALIDATION_DATA.getPassportNumber()))
                .thenReturn(Optional.empty());
        this.service.createPassportValidationData(VALIDATION_DATA);

        // Then
        verify(this.repository, times(1))
                .save(VALIDATION_DATA);
    }

    @Test
    void shouldNotCreatePassportValidationDataIfPresentTest() {
        // Given
        // When
        when(this.repository.getPassportValidationDataByPassportNumber(VALIDATION_DATA.getPassportNumber()))
                .thenReturn(Optional.of(VALIDATION_DATA));

        // Then
        assertThrows(
                InvalidPassportException.class,
                () -> this.service.createPassportValidationData(VALIDATION_DATA)
        );
    }

    @Test
    void shouldFetchStudentFromPassportValidationIfPresentTest() {
        // Given
        // When
        when(this.repository.getPassportValidationDataByPassportNumber(VALIDATION_DATA.getPassportNumber()))
                .thenReturn(Optional.of(VALIDATION_DATA));
        StudentDto actual = this.service
                .getStudentFromPassportValidation(VALIDATION_DATA.getPassportNumber());


        // Then
        assertEquals(STUDENT_DTO_FROM_PASSPORT_VALIDATION, actual);
    }

    @Test
    void shouldThrowExceptionWhenFetchingPassportValidationIfNotPresentTest() {
        // Given
        // When
        when(this.repository.getPassportValidationDataByPassportNumber(VALIDATION_DATA.getPassportNumber()))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                PassportValidationDataNotFoundException.class,
                () -> this.service.getStudentFromPassportValidation(VALIDATION_DATA.getPassportNumber())
        );
    }

}