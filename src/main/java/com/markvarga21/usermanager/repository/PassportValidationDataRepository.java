package com.markvarga21.usermanager.repository;

import com.markvarga21.usermanager.entity.PassportValidationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A repository which is used to store the data extracted
 * from the passport while validation.
 */
@Repository
public interface PassportValidationDataRepository extends
        JpaRepository<PassportValidationData, Long> {
    /**
     * Deletes the passport validation data by the passport number.
     *
     * @param passportNumber the passport number.
     */
    void deletePassportValidationDataByPassportNumber(String passportNumber);
}
