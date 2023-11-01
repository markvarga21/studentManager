package com.markvarga21.studentmanager.service.validation.face.impl;

import com.markvarga21.studentmanager.entity.FacialValidationData;
import com.markvarga21.studentmanager.repository.FacialValidationDataRepository;
import com.markvarga21.studentmanager.service.validation.face.FacialValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The FacialValidationServiceImpl class is used to manipulate
 * facial validation data.
 */
@Service
@RequiredArgsConstructor
public class FacialValidationServiceImpl implements FacialValidationService {
    /**
     * A repository which is used to access facial
     * validation data.
     */
    private final FacialValidationDataRepository repository;

    /**
     * Saves the facial validation data to the database.
     *
     * @param data The facial validation data to be saved.
     */
    @Override
    @Transactional
    public void saveFacialValidationData(
            final FacialValidationData data
    ) {
        Optional<FacialValidationData> facialValidationData
                = this.repository.getFacialValidationDataByPassportNumber(data.getPassportNumber());
        if (facialValidationData.isPresent()) {
            this.repository.deleteFacialValidationDataByPassportNumber(data.getPassportNumber());
        }
        this.repository.save(data);
    }

    /**
     * Gets the facial validation data by passport number.
     *
     * @param passportNumber The passport number of the facial validation data.
     * @return The facial validation data.
     */
    @Override
    public FacialValidationData getFacialValidationDataByPassportNumber(
            final String passportNumber
    ) {
        Optional<FacialValidationData> facialValidationDataOptional = this.repository
                .getFacialValidationDataByPassportNumber(passportNumber);
        return facialValidationDataOptional.orElse(null);
    }

    /**
     * Deletes the facial validation data by passport number.
     *
     * @param passportNumber The passport number of the facial validation data.
     */
    @Override
    @Transactional
    public void deleteFacialValidationDataByPassportNumber(
            final String passportNumber
    ) {
        this.repository
                .deleteFacialValidationDataByPassportNumber(passportNumber);
    }
}
