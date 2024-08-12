package com.markvarga21.studentmanager.service.validation.face;

import com.markvarga21.studentmanager.entity.FacialValidationData;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * The {@code FacialValidationService} interface is used to manipulate
 * facial validation data.
 */
@Service
public interface FacialValidationService {
    /**
     * Saves the facial validation data in the database.
     *
     * @param data The facial validation data to be saved.
     */
    void saveFacialValidationData(FacialValidationData data);

    /**
     * Fetches the facial validation data by passport number.
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
     * @return An informational message.
     */
    String deleteFacialValidationDataByPassportNumber(
            String passportNumber
    );

    /**
     * Sets the facial validity by passport number.
     *
     * @param passportNumber The passport number of the facial validation data.
     * @return A feedback message.
     */
    String setFacialValidationToValid(
            String passportNumber
    );

    /**
     * Sets the facial validity by passport number to invalid.
     *
     * @param studentId The id of the student.
     * @return A feedback message.
     */
    String setFacialValidationToInvalid(
            Long studentId
    );

    /**
     * Fetches all facial validation data.
     *
     * @param page The page number.
     * @param size The number of elements inside a single page.
     * @return A page containing a subset of {@code FacialValidationData}.
     */
    Page<FacialValidationData> getAllFacialValidationData(
            Integer page,
            Integer size
    );
}
