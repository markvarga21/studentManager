package com.markvarga21.usermanager.controller;


import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.dto.PassportValidationResponse;
import com.markvarga21.usermanager.service.azure.FormRecognizerService;
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
public class FormRecognizerController {
    /**
     * Form recognizer service.
     */
    private final FormRecognizerService formRecognizerService;

    /**
     * Extracts and returns the data from the passport.
     *
     * @param passport the photo of the passport.
     * @return the extracted {@code AppUserDto} object.
     */
    @PostMapping("/extractData")
    public ResponseEntity<AppUserDto> getDataFromPassport(
            @RequestParam("passport") final MultipartFile passport
    ) {
        AppUserDto appUserDto = this.formRecognizerService
                .extractDataFromPassport(passport);
        return new ResponseEntity<>(appUserDto, HttpStatus.OK);
    }

    /**
     * Validates the data entered by the user against the data
     * which can be found on the passport.
     *
     * @param passport the photo of the passport.
     * @param appUserJson the user itself in a JSON string.
     * @return a {@code PassportValidationResponse} object.
     */
    @PostMapping("/validate")
    public ResponseEntity<PassportValidationResponse> validatePassport(
            @RequestParam("passport") final MultipartFile passport,
            @RequestParam("appUserJson") final String appUserJson
    ) {
        PassportValidationResponse passportValidationResponse =
                this.formRecognizerService.validatePassport(
                        passport,
                        appUserJson
                );
        return new ResponseEntity<>(passportValidationResponse, HttpStatus.OK);
    }
}
