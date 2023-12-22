package com.markvarga21.studentmanager.controller;


import com.markvarga21.studentmanager.dto.PassportValidationResponse;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.PassportValidationData;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.service.validation.face.FacialValidationService;
import com.markvarga21.studentmanager.service.validation.passport.PassportValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * A controller which is used to make access
 * validation (face and form) data.
 */
@RestController
@RequestMapping("/api/v1/validations")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
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
    public ResponseEntity<String> deletePassportValidationData(
            @PathVariable("passportNumber") final String passportNumber
    ) {
        String message = this.passportValidationService
                .deletePassportValidationData(passportNumber);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Validates the data entered by the user against the data
     * which can be found on the passport.
     *
     * @param studentJson The student itself.
     * @param passport The photo of the passport.
     * @return A {@code PassportValidationResponse} object.
     */
    @PostMapping("/validate")
    public ResponseEntity<PassportValidationResponse> validatePassport(
            @RequestParam final String studentJson,
            @RequestParam("passport") final MultipartFile passport
    ) {
        PassportValidationResponse passportValidationResponse =
                this.formRecognizerService.validatePassport(studentJson, passport);
        return new ResponseEntity<>(passportValidationResponse, HttpStatus.OK);
    }

    /**
     * Validates the passport manually (usually by an admin).
     *
     * @param studentId The id of the student.
     * @return {@code HttpStatus.OK} if the validation was successful,
     */
    @PostMapping("/validateManually")
    public ResponseEntity<String> validatePassportManually(
            @RequestParam("studentId") final Long studentId
    ) {
        log.info("Manually validating passport for user with ID '{}'", studentId);
        String message = this.formRecognizerService.validatePassportManually(studentId);
        return new ResponseEntity<>(message, HttpStatus.OK);
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

    /**
     * Creates a new passport validation data.
     *
     * @param passportValidationData The passport validation data itself.
     * @return The created passport validation data.
     */
    @PostMapping
    public ResponseEntity<PassportValidationData> createPassportValidationData(
            @RequestBody final PassportValidationData passportValidationData
    ) {
        PassportValidationData createdPassportValidationData =
                this.passportValidationService
                        .createPassportValidationData(passportValidationData);
        return new ResponseEntity<>(
                createdPassportValidationData,
                HttpStatus.CREATED
        );
    }

    /**
     * Retrieves {@code StudentDto} object from
     * the validation data by passport number.
     *
     * @param passportNumber The passport number of the student.
     * @return The {@code StudentDto} object.
     */
    @GetMapping("/{passportNumber}")
    public ResponseEntity<StudentDto> getPassportValidationByPassportNumber(
            @PathVariable("passportNumber") final String passportNumber
    ) {
        StudentDto studentDto = this.passportValidationService
                .getPassportValidationByPassportNumber(passportNumber);
        return new ResponseEntity<>(studentDto, HttpStatus.OK);
    }
}
