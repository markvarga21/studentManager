package com.markvarga21.studentmanager.controller;


import com.markvarga21.studentmanager.entity.PassportValidationData;
import com.markvarga21.studentmanager.service.validation.passport.PassportValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A controller which is used to make access
 * validation (face and form) data.
 */
@RestController
@RequestMapping("/api/v1/passports/validations")
@RequiredArgsConstructor
@CrossOrigin
public class PassportValidationController {
    /**
     * A service which is used to access passport
     * validation data.
     */
    private final PassportValidationService passportValidationService;

    /**
     * Retrieves all the passport validation data.
     *
     * @return All the passport validation data.
     */
    @GetMapping
    public List<PassportValidationData> getAllPassportValidationData() {
        return this.passportValidationService
                .getAllPassportValidationData();
    }

    /**
     * Deletes the passport validation data with the given ID.
     *
     * @param passportNumber The passport number of the student.
     */
    @DeleteMapping("/{passportNumber}")
    public void deletePassportValidationData(
            @PathVariable("passportNumber") final String passportNumber
    ) {
        this.passportValidationService
                .deletePassportValidationData(passportNumber);
    }

    /**
     * Retrieves the saved passport image byte array.
     *
     * @param passportNumber The ID of the passport validation data.
     * @return The saved passport image byte array.
     */
    @GetMapping("/{passportNumber}/passport")
    public ResponseEntity<?> getPassport(
            @PathVariable("passportNumber") final String passportNumber
    ) {
        byte[] passport = this.passportValidationService
                .getPassport(passportNumber);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(MediaType.IMAGE_PNG_VALUE))
                .body(passport);
    }
}
