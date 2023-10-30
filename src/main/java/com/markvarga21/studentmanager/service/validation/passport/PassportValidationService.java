package com.markvarga21.studentmanager.service.validation.passport;

import com.markvarga21.studentmanager.entity.PassportValidationData;
import org.springframework.stereotype.Service;

import java.util.List;

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
     */
    void deletePassportValidationData(String passportNumber);

    /**
     * Retrieves the saved passport image byte array.
     *
     * @param passportNumber The unique passport number.
     * @return The saved passport image byte array.
     */
    byte[] getPassport(String passportNumber);
}
