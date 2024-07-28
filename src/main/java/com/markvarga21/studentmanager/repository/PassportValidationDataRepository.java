package com.markvarga21.studentmanager.repository;

import com.markvarga21.studentmanager.entity.PassportValidationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A repository which is used to store the data extracted
 * from the passport.
 */
@Repository
public interface PassportValidationDataRepository extends
        JpaRepository<PassportValidationData, String> {
    /**
     * Deletes a passport validation data by the passport number.
     *
     * @param passportNumber The passport number.
     */
    void deletePassportValidationDataByPassportNumber(String passportNumber);

    /**
     * Retrieves a passport validation data by the passport number.
     *
     * @param passportNumber The passport number.
     * @return The passport validation data.
     */
    Optional<PassportValidationData> getPassportValidationDataByPassportNumber(
            String passportNumber
    );
}
