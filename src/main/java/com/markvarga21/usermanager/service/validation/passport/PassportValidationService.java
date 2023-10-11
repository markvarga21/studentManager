package com.markvarga21.usermanager.service.validation.passport;

import com.markvarga21.usermanager.entity.PassportValidationData;
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
     * @param id The ID of the passport validation data.
     */
    void deletePassportValidationData(Long id);
}
