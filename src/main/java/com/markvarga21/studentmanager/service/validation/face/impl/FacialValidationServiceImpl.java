package com.markvarga21.studentmanager.service.validation.face.impl;

import com.markvarga21.studentmanager.entity.FacialValidationData;
import com.markvarga21.studentmanager.repository.FacialValidationDataRepository;
import com.markvarga21.studentmanager.service.validation.face.FacialValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The FacialValidationServiceImpl class is used to manipulate
 * facial validation data.
 */
@Service
@RequiredArgsConstructor
@Slf4j
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
     * Retrieves all the facial validation data.
     *
     * @return All the facial validation data.
     */
    @Override
    public List<FacialValidationData> getAllFacialValidationData() {
        return this.repository.findAll();
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

    /**
     * Sets the facial validity by passport number.
     *
     * @param passportNumber The passport number of the facial validation data.
     */
    @Override
    @Transactional
    public void setFacialValidationToValid(
            final String passportNumber
    ) {
        FacialValidationData data =
            this.getFacialValidationDataByPassportNumber(passportNumber);
        if (data == null) {
            log.info("Facial validation data not found, creating new one.");
            data = new FacialValidationData();
            data.setPassportNumber(passportNumber);
            data.setIsValid(true);
            data.setPercentage(1.0);
            this.repository.save(data);
            return;
        }
        log.info("Setting facial validation data to valid.");
        data.setIsValid(true);
        data.setPercentage(1.0);
        this.repository.save(data);
    }
}
