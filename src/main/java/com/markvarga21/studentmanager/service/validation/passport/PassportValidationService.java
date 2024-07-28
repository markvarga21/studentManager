package com.markvarga21.studentmanager.service.validation.passport;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.PassportValidationData;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * A service which is used to manipulate passport
 * validation data.
 */
@Service
public interface PassportValidationService {
    /**
     * Fetches all passport validation data.
     *
     * @param page The page number.
     * @param size The number of elements in a single page.
     * @return The page of passport validation data.
     */
    Page<PassportValidationData> getAllPassportValidationData(
            Integer page,
            Integer size
    );

    /**
     * Deletes a passport validation data with the given ID.
     *
     * @param passportNumber The passport number of the student.
     * @return An informational message.
     */
    String deletePassportValidationData(String passportNumber);

    /**
     * Retrieves a student by passport number.
     *
     * @param passportNumber The passport number of the student.
     * @return A feedback message.
     */
    Optional<PassportValidationData> getPassportValidationDataByPassportNumber(
            String passportNumber
    );

    /**
     * Creates a new passport validation data.
     *
     * @param data The passport validation data.
     * @return The newly created passport validation data.
     */
    PassportValidationData createPassportValidationData(
            PassportValidationData data
    );

    /**
     * Retrieves a {@code StudentDto} object from
     * the validation data by passport number.
     *
     * @param passportNumber The passport number of the student.
     * @return The {@code StudentDto} object.
     */
    StudentDto getStudentFromPassportValidation(String passportNumber);
}
