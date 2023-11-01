package com.markvarga21.studentmanager.repository;

import com.markvarga21.studentmanager.entity.FacialValidationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The FacialValidationDataRepository interface is used to
 * store the facial validation data in the database.
 */
@Repository
public interface FacialValidationDataRepository
        extends JpaRepository<FacialValidationData, Long> {
    /**
     * Gets the facial validation data by passport number.
     *
     * @param passportNumber The passport number of the facial validation data.
     * @return The facial validation data.
     */
    Optional<FacialValidationData> getFacialValidationDataByPassportNumber(
            String passportNumber
    );

    /**
     * Deletes the facial validation data by passport number.
     *
     * @param passportNumber The passport number of the facial validation data.
     */
    void deleteFacialValidationDataByPassportNumber(
            String passportNumber
    );
}
