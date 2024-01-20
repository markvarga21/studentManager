package com.markvarga21.studentmanager.service.validation.passport;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.PassportValidationData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A service which is used to access passport
 * validation data.
 */
@Service
public interface PassportValidationService {
    /**
     * Retrieves all the passport validation data.
     *
     * @return All the passport validation data.
     */
    List<PassportValidationData> getAllPassportValidationData();

    /**
     * Deletes the passport validation data with the given ID.
     *
     * @param passportNumber The passport number of the student.
     * @return The message.
     */
    String deletePassportValidationData(String passportNumber);

    /**
     * Retrieves a student by passport number.
     *
     * @param passportNumber The passport number of the student.
     * @return The student.
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
     * Retrieves {@code StudentDto} object from
     * the validation data by passport number.
     *
     * @param passportNumber The passport number of the student.
     * @return The {@code StudentDto} object.
     */
    StudentDto getPassportValidationByPassportNumber(String passportNumber);
}
