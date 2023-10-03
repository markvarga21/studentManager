package com.markvarga21.usermanager.service.validation.passport.impl;

import com.markvarga21.usermanager.entity.PassportValidationData;
import com.markvarga21.usermanager.repository.PassportValidationDataRepository;
import com.markvarga21.usermanager.service.validation.passport.PassportValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A service which is used to access passport
 * validation data.
 */
@Service
@RequiredArgsConstructor
public class PassportValidationServiceImpl
        implements PassportValidationService {
    /**
     * A repository which is used to store the data extracted
     * from the passport while validation.
     */
    private final PassportValidationDataRepository passportValidationDataRepository;

    /**
     * Retrieves all the passport validation data.
     *
     * @return all the passport validation data.
     */
    @Override
    public List<PassportValidationData> getAllPassportValidationData() {
        return this.passportValidationDataRepository
                .findAll();
    }

    /**
     * Deletes the passport validation data with the given ID.
     *
     * @param id the ID of the passport validation data.
     */
    @Override
    public void deletePassportValidationData(final Long id) {
        this.passportValidationDataRepository
                .deleteById(id);
    }
}
