package com.markvarga21.studentmanager.service.validation.passport.impl;

import com.markvarga21.studentmanager.entity.PassportValidationData;
import com.markvarga21.studentmanager.exception.PassportValidationDataNotFoundException;
import com.markvarga21.studentmanager.repository.PassportValidationDataRepository;
import com.markvarga21.studentmanager.service.validation.passport.PassportValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
     * @return All the passport validation data.
     */
    @Override
    public List<PassportValidationData> getAllPassportValidationData() {
        return this.passportValidationDataRepository
                .findAll();
    }

    /**
     * Deletes the passport validation data with the given ID.
     *
     * @param passportNumber The passport number of the student.
     */
    @Override
    @Transactional
    public void deletePassportValidationData(final String passportNumber) {
        Optional<PassportValidationData> passportValidationDataOptional =
                this.passportValidationDataRepository
                        .getPassportValidationDataByPassportNumber(passportNumber);
        if (passportValidationDataOptional.isPresent()) {
            this.passportValidationDataRepository
                    .deletePassportValidationDataByPassportNumber(passportNumber);
            return;
        }
        String message = String.format(
                "Passport validation data with passport number %s not found.",
                passportNumber
        );
        throw new PassportValidationDataNotFoundException(message);
    }

    /**
     * Retrieves the saved passport image byte array.
     *
     * @param passportNumber The unique passport number.
     * @return The saved passport image byte array.
     */
    @Override
    public byte[] getPassport(final String passportNumber) {
        return new byte[100];
    }
}
