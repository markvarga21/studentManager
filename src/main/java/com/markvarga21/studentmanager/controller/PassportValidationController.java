package com.markvarga21.studentmanager.controller;


import com.markvarga21.studentmanager.entity.PassportValidationData;
import com.markvarga21.studentmanager.service.validation.passport.PassportValidationService;
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
     * @param id The ID of the passport validation data.
     */
    @DeleteMapping("/{id}")
    public void deletePassportValidationData(
            @PathVariable("id") final Long id
    ) {
        this.passportValidationService
                .deletePassportValidationData(id);
    }
}
