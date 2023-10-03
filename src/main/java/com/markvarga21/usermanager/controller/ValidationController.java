package com.markvarga21.usermanager.controller;


import com.markvarga21.usermanager.entity.PassportValidationData;
import com.markvarga21.usermanager.service.validation.passport.PassportValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * A controller which is used to make access
 * validation (face and form) data.
 */
@RestController
@RequestMapping("/api/v1/validations")
@RequiredArgsConstructor
@CrossOrigin
public class ValidationController {
    /**
     * A service which is used to access passport
     * validation data.
     */
    private final PassportValidationService passportValidationService;

    /**
     * Retrieves all the passport validation data.
     *
     * @return all the passport validation data.
     */
    @GetMapping("/passport")
    public List<PassportValidationData> getAllPassportValidationData() {
        return this.passportValidationService
                .getAllPassportValidationData();
    }

    /**
     * Deletes the passport validation data with the given ID.
     *
     * @param id the ID of the passport validation data.
     */
    @DeleteMapping("/passport/{id}")
    public void deletePassportValidationData(
            @PathVariable("id") final Long id
    ) {
        this.passportValidationService
                .deletePassportValidationData(id);
    }
}
