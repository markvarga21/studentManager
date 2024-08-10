package com.markvarga21.studentmanager.controller;


import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.exception.util.ApiError;
import com.markvarga21.studentmanager.exception.util.AuthError;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(
    name = "Form services",
    description = "The Form Recognizer related endpoints."
)
public class FormRecognizerController {
    /**
     * Form recognizer service.
     */
    private final FormRecognizerService formRecognizerService;

    /**
     * Extracts- and returns the data from the passport.
     *
     * @param passport The photo of the passport.
     * @return The extracted {@code StudentDto} object.
     */
    @Operation(
        summary = "Extracts and returns the data from the passport.",
        responses = {
            @ApiResponse(responseCode = "200", description = "The data of the student extracted from the passport.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
    @PostMapping("/extractData")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StudentDto> getDataFromPassport(
            @RequestParam("passport") final MultipartFile passport
    ) {
        StudentDto studentDto = this.formRecognizerService
                .extractDataFromPassport(passport);
        return new ResponseEntity<>(studentDto, HttpStatus.OK);
    }
}
