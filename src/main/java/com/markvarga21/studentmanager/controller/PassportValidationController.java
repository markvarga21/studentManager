package com.markvarga21.studentmanager.controller;


import com.markvarga21.studentmanager.dto.PassportValidationResponse;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.PassportValidationData;
import com.markvarga21.studentmanager.exception.util.ApiError;
import com.markvarga21.studentmanager.exception.util.AuthError;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.service.validation.passport.PassportValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

/**
 * A controller which is used to access
 * validation (face and form) data.
 */
@RestController
@RequestMapping("/api/v1/validations")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@Tag(
    name = "Validation services",
    description = "The validation related endpoints."
)
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
     * @param page The page number.
     * @param size The number of elements in a single page.
     * @return A page containing passport validations.
     */
    @Operation(
        summary = "Retrieves all the passport validation data.",
        responses = {
            @ApiResponse(responseCode = "200", description = "A page of passport validation data.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<PassportValidationData> getAllPassportValidationData(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "10") final Integer size
    ) {
        return this.passportValidationService
                .getAllPassportValidationData(page, size);
    }

    /**
     * Deletes a passport validation data with the given ID.
     *
     * @param passportNumber The passport number of the student.
     * @return A {@code ResponseEntity} object containing some feedback.
     */
    @Operation(
        summary = "Deletes the passport validation data with the given ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "A status message about the deletion.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
    @DeleteMapping("/{passportNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @Operation(
        summary = "Validates the data entered by the user against the data which can be found on the passport.",
        responses = {
            @ApiResponse(responseCode = "200", description = "The validation information for the given passport.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PassportValidationResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
    @PostMapping("/validate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PassportValidationResponse> validatePassport(
            @RequestParam final String studentJson,
            @RequestParam("passport") final MultipartFile passport
    ) {
        PassportValidationResponse passportValidationResponse =
                this.formRecognizerService
                        .validatePassport(studentJson, passport);
        return new ResponseEntity<>(passportValidationResponse, HttpStatus.OK);
    }

    /**
     * Validates the passport manually.
     *
     * @param studentId The id of the student.
     * @return A {@code ResponseEntity} containing some feedback.
     */
    @Operation(
        summary = "Validates the passport manually.",
        responses = {
            @ApiResponse(responseCode = "200", description = "A status message of manually setting the validity.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
    @PostMapping("/validateManually")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> validatePassportManually(
            @RequestParam("studentId") final Long studentId
    ) {
        log.info(
                "Manually validating passport for user with ID '{}'",
                studentId
        );
        String message = this.formRecognizerService
                .validatePassportManually(studentId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Checks whether the user is valid or not.
     *
     * @param passportNumber The passport number.
     * @return A {@code ResponseEntity} containing some feedback.
     */
    @Operation(
        summary = "Checks if the student is valid or not.",
        responses = {
            @ApiResponse(responseCode = "200", description = "The validity of the student.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
    @GetMapping("/isUserValid/{passportNumber}")
    @PreAuthorize("hasRole('ROLE_USER')")
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
     * @return The recently created passport validation data.
     */
    @Operation(
        summary = "Creates a new passport validation data.",
        responses = {
            @ApiResponse(responseCode = "200", description = "The generated passport validation data.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PassportValidationData.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
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
     * Retrieves the {@code StudentDto} object from
     * the validation data identified by the passport number.
     *
     * @param passportNumber The passport number of the student.
     * @return The {@code StudentDto} object.
     */
    @Operation(
        summary = "Retrieves StudentDto object from the validation data identified by passport number.",
        responses = {
            @ApiResponse(responseCode = "200", description = "The fetched passport validation data for the student.", content = {
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
    @GetMapping("/{passportNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StudentDto> getPassportValidationByPassportNumber(
            @PathVariable("passportNumber") final String passportNumber
    ) {
        StudentDto studentDto = this.passportValidationService
                .getStudentFromPassportValidation(passportNumber);
        return new ResponseEntity<>(studentDto, HttpStatus.OK);
    }
}
