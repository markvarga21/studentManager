package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.entity.FacialValidationData;
import com.markvarga21.studentmanager.exception.util.ApiError;
import com.markvarga21.studentmanager.exception.util.AuthError;
import com.markvarga21.studentmanager.service.validation.face.FacialValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller which is used to make facial validations.
 */
@RestController
@RequestMapping("/api/v1/facialValidations")
@RequiredArgsConstructor
@CrossOrigin
@Tag(
    name = "Facial validation services",
    description = "The facial validation related endpoints."
)
public class FacialValidationController {
    /**
     * A service which is used to access facial
     * validation data.
     */
    private final FacialValidationService facialValidationService;

    /**
     * Retrieves all the facial validation data.
     *
     * @param page The page number.
     * @param size The number of elements inside a single page.
     * @return A page storing facial validations.
     */
    @Operation(
        summary = "Retrieves all the facial validation data.",
        responses = {
            @ApiResponse(responseCode = "200", description = "A page of facial validation data.", content = {
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
    public Page<FacialValidationData> getAllFacialValidationData(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "10") final Integer size
    ) {
        return this.facialValidationService
                .getAllFacialValidationData(page, size);
    }

    /**
     * Retrieves the facial validation data for the given passport number.
     *
     * @param passportNumber The passport number of the student.
     * @return The facial validation data.
     */
    @SuppressWarnings("checkstyle:LineLength")
    @Operation(
        summary = "Retrieves the facial validation data for the given passport number.",
        responses = {
            @ApiResponse(responseCode = "200", description = "The fetched passport validation data.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FacialValidationData.class))
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
    @PreAuthorize("hasRole('ROLE_USER')")
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
     * Deletes a facial validation data with the given passport number.
     *
     * @param passportNumber The passport number of the student.
     * @return A message which indicates whether the
     * deletion was successful or not.
     */
    @Operation(
        summary = "Deletes a facial validation data for the given passport number.",
        responses = {
            @ApiResponse(responseCode = "200", description = "An informational message of the deletion.", content = {
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
     * @return A message which indicates whether the operation
     * was successful or not.
     */
    @Operation(
        summary = "Sets the facial validation data to valid.",
        responses = {
            @ApiResponse(responseCode = "200", description = "A status message.", content = {
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
    @PostMapping("/setFacialValidationDataToValid")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> setFacialValidationDataToValid(
            @RequestParam final String passportNumber
    ) {
        String message = this.facialValidationService
                .setFacialValidationToValid(passportNumber);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
