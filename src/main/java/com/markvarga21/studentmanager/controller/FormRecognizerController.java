package com.markvarga21.studentmanager.controller;


import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "Form services", description = "The Form Recognizer related endpoints.")
public class FormRecognizerController {
    /**
     * Form recognizer service.
     */
    private final FormRecognizerService formRecognizerService;

    /**
     * Extracts and returns the data from the passport.
     *
     * @param passport The photo of the passport.
     * @return The extracted {@code StudentDto} object.
     */
    @Operation(
            summary = "Extracts and returns the data from the passport.",
            description = "Extracts and returns the data from the passport."
    )
    @PostMapping("/extractData")
    public ResponseEntity<StudentDto> getDataFromPassport(
            @RequestParam("passport") final MultipartFile passport
    ) {
        StudentDto studentDto = this.formRecognizerService
                .extractDataFromPassport(passport);
        return new ResponseEntity<>(studentDto, HttpStatus.OK);
    }
}
