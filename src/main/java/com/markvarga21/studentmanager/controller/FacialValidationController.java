package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.entity.FacialValidationData;
import com.markvarga21.studentmanager.service.validation.face.FacialValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * A controller which is used to make facial validations.
 */
@RestController
@RequestMapping("/api/v1/facialValidations")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class FacialValidationController {
    /**
     * A service which is used to access facial
     * validation data.
     */
    private final FacialValidationService facialValidationService;

    /**
     * Retrieves all the facial validation data.
     *
     * @return All the facial validation data.
     */
    @GetMapping
    public List<FacialValidationData> getAllFacialValidationData() {
        return this.facialValidationService.getAllFacialValidationData();
    }

    /**
     * Retrieves the facial validation data with the given passport number.
     *
     * @param passportNumber The passport number of the student.
     * @return The facial validation data.
     */
    @SuppressWarnings("checkstyle:LineLength")
    @GetMapping("/{passportNumber}")
    public ResponseEntity<FacialValidationData> getFacialValidationDataByPassportNumber(
            @PathVariable final String passportNumber
    ) {
        FacialValidationData facialValidationData = this.facialValidationService
                .getFacialValidationDataByPassportNumber(passportNumber);
        if (facialValidationData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(facialValidationData, HttpStatus.OK);
    }

    /**
     * Deletes the facial validation data with the given passport number.
     *
     * @param passportNumber The passport number of the student.
     * @return A message which indicates whether the
     * deletion was successful or not.
     */
    @DeleteMapping("/{passportNumber}")
    public ResponseEntity<String> deleteFacialValidationDataByPassportNumber(
            @PathVariable final String passportNumber
    ) {
        String message = this.facialValidationService
                .deleteFacialValidationDataByPassportNumber(passportNumber);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Sets the facial validation data to valid.
     *
     * @param passportNumber The passport number of the student.
     * @return A message which indicates whether the setting
     * was successful or not.
     */
    @PostMapping("/setFacialValidationDataToValid")
    public ResponseEntity<String> setFacialValidationDataToValid(
            @RequestParam final String passportNumber
    ) {
        String message = this.facialValidationService
                .setFacialValidationToValid(passportNumber);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
