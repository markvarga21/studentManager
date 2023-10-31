package com.markvarga21.studentmanager.controller;


import com.markvarga21.studentmanager.dto.PassportValidationResponse;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.PassportValidationData;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.service.validation.passport.PassportValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A controller which is used to make access
 * validation (face and form) data.
 */
@RestController
@RequestMapping("/api/v1/validations")
@RequiredArgsConstructor
@CrossOrigin
public class PassportValidationController {
    /**
     * A service which is used to access passport
     * validation data.
     */
    private final PassportValidationService passportValidationService;

    /**
     * Form recognizer service.
     */
    private final FormRecognizerService formRecognizerService;

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
     * Validates the data entered by the user against the data
     * which can be found on the passport.
     *
     * @param student The student itself.
     * @return A {@code PassportValidationResponse} object.
     */
    @PostMapping("/validate")
    public ResponseEntity<PassportValidationResponse> validatePassport(
            @RequestBody @Valid final StudentDto student
    ) {
        PassportValidationResponse passportValidationResponse =
                this.formRecognizerService.validatePassport(student);
        return new ResponseEntity<>(passportValidationResponse, HttpStatus.OK);
    }

    /**
     * Validates the passport manually (usually by an admin).
     *
     * @param passportNumber The passport number.
     * @return {@code HttpStatus.OK} if the validation was successful,
     */
    @PostMapping("/validateManually")
    public ResponseEntity<Void> validatePassportManually(
            @RequestParam("passportNumber") final String passportNumber
    ) {
        this.formRecognizerService.validatePassportManually(passportNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Checks if the user is valid.
     *
     * @param passportNumber The passport number.
     * @return {@code HttpStatus.OK} if the user is valid.
     */
    @GetMapping("/isUserValid/{passportNumber}")
    public ResponseEntity<Boolean> isUserValid(
            @PathVariable("passportNumber") final String passportNumber
    ) {
        return new ResponseEntity<>(
                this.formRecognizerService.isUserValid(passportNumber),
                HttpStatus.OK
        );
    }
}
