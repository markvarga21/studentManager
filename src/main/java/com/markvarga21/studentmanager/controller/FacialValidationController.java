package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.entity.FacialValidationData;
import com.markvarga21.studentmanager.service.validation.face.FacialValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * Retrieves the facial validation data with the given passport number.
     *
     * @param passportNumber The passport number of the student.
     * @return The facial validation data.
     */
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
     * Sets the facial validation data to valid.
     *
     * @param passportNumber The passport number of the student.
     */
    @PostMapping("/setFacialValidationDataToValid")
    public void setFacialValidationDataToValid(
            @RequestParam final String passportNumber
    ) {
        this.facialValidationService.setFacialValidationToValid(passportNumber);
    }
}
