package com.markvarga21.studentmanager.service.validation.face;

import com.markvarga21.studentmanager.entity.FacialValidationData;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @return The message.
     */
    String deleteFacialValidationDataByPassportNumber(
            String passportNumber
    );

    /**
     * Sets the facial validity by passport number.
     *
     * @param passportNumber The passport number of the facial validation data.
     * @return The message.
     */
    String setFacialValidationToValid(
            String passportNumber
    );

    /**
     * Retrieves all the facial validation data.
     *
     * @return All the facial validation data.
     */
    List<FacialValidationData> getAllFacialValidationData();
}
