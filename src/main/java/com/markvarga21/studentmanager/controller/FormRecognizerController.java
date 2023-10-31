package com.markvarga21.studentmanager.controller;


import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.dto.PassportValidationResponse;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * A controller which is used to make Form related operations.
 */
@RestController
@RequestMapping("/api/v1/form")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class FormRecognizerController {
    /**
     * Form recognizer service.
     */
    private final FormRecognizerService formRecognizerService;

    /**
     * Extracts and returns the data from the passport.
     *
     * @param passport The photo of the passport.
     * @param selfie The selfie of the student.
     * @return The extracted {@code StudentDto} object.
     */
    @PostMapping("/extractData")
    public ResponseEntity<StudentDto> getDataFromPassport(
            @RequestParam("passport") final MultipartFile passport,
            @RequestParam("selfie") final MultipartFile selfie
    ) {
        log.info("Controller extraction method called");
        StudentDto studentDto = this.formRecognizerService
                .extractDataFromPassport(passport, selfie);
        return new ResponseEntity<>(studentDto, HttpStatus.OK);
    }
}
