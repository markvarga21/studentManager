package com.markvarga21.studentmanager.service.validation.face;

import com.markvarga21.studentmanager.entity.FacialValidationData;
import org.springframework.stereotype.Service;

/**
 * The FacialValidationService interface is used to manipulate
 * facial validation data.
 */
@Service
public interface FacialValidationService {
    /**
     * Saves the facial validation data to the database.
     *
     * @param data The facial validation data to be saved.
     */
    void saveFacialValidationData(FacialValidationData data);

    /**
     * Gets the facial validation data by passport number.
     *
     * @param passportNumber The passport number of the facial validation data.
     * @return The facial validation data.
     */
    FacialValidationData getFacialValidationDataByPassportNumber(
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
