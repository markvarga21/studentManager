package com.markvarga21.studentmanager.service.validation.passport.impl;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.PassportValidationData;
import com.markvarga21.studentmanager.exception.InvalidPassportException;
import com.markvarga21.studentmanager.exception.PassportValidationDataNotFoundException;
import com.markvarga21.studentmanager.repository.PassportValidationDataRepository;
import com.markvarga21.studentmanager.service.validation.passport.PassportValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * A service which is used to access passport
 * validation data.
 */
@Service
@RequiredArgsConstructor
@Slf4j
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
    public String deletePassportValidationData(final String passportNumber) {
        Optional<PassportValidationData> passportValidationDataOptional =
                this.passportValidationDataRepository
                        .getPassportValidationDataByPassportNumber(passportNumber);
        if (passportValidationDataOptional.isPresent()) {
            this.passportValidationDataRepository
                    .deletePassportValidationDataByPassportNumber(passportNumber);
            return String.format("Passport validation data with passport number %s deleted.",
                    passportNumber);
        }
        String message = String.format(
                "Passport validation data with passport number %s not found.",
                passportNumber
        );
        throw new PassportValidationDataNotFoundException(message);
    }

    /**
     * Retrieves a student by passport number.
     *
     * @param passportNumber The passport number of the student.
     * @return The student optional.
     */
    @Override
    public Optional<PassportValidationData> getPassportValidationDataByPassportNumber(
            final String passportNumber
    ) {
        log.info("Retrieving passport validation data with passport number: {}",
                passportNumber);
        return this.passportValidationDataRepository
                .getPassportValidationDataByPassportNumber(passportNumber);
    }

    /**
     * Creates a new passport validation data.
     *
     * @param data The passport validation data.
     * @return The newly created passport validation data.
     */
    @Override
    @Transactional
    public PassportValidationData createPassportValidationData(
            final PassportValidationData data
    ) {
        data.setTimestamp(LocalDateTime.now());
        Optional<PassportValidationData> passportValidationDataOptional
                = this.passportValidationDataRepository
                        .getPassportValidationDataByPassportNumber(
                                data.getPassportNumber()
                        );
        if (passportValidationDataOptional.isPresent()) {
            String message = "Passport validation data with passport number %s already exists.";
            throw new InvalidPassportException(message);
        }
        log.info("Saving passport validation data: {}", data);
        return this.passportValidationDataRepository
                .save(data);
    }

    /**
     * Retrieves {@code StudentDto} object from
     * the validation data by passport number.
     *
     * @param passportNumber The passport number of the student.
     * @return The {@code StudentDto} object.
     */
    @Override
    public StudentDto getStudentFromPassportValidation(
            final String passportNumber
    ) {
        Optional<PassportValidationData> dataOptional =
                this.passportValidationDataRepository
                        .getPassportValidationDataByPassportNumber(passportNumber);
        if (dataOptional.isEmpty()) {
            log.error("Passport validation data with passport number {} not found.",
                    passportNumber);
            throw new PassportValidationDataNotFoundException(
                    String.format(
                            "Passport validation data with passport number %s not found.",
                            passportNumber
                    ));
        }
        PassportValidationData data = dataOptional.get();
        return PassportValidationData
                .getStudentDtoFromValidationData(data);
    }
}
