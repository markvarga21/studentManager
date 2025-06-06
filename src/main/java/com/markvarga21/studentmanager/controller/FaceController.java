package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.dto.FaceApiResponse;
import com.markvarga21.studentmanager.exception.util.ApiError;
import com.markvarga21.studentmanager.exception.util.AuthError;
import com.markvarga21.studentmanager.service.faceapi.FaceApiService;
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
 * A controller which is used to make Face related operations.
 */
@RestController
@RequestMapping("/api/v1/faces")
@RequiredArgsConstructor
@CrossOrigin
@Tag(
    name = "Facial services",
    description = "The Face API related endpoints."
)
public class FaceController {
    /**
     * The face service.
     */
    private final FaceApiService faceApiService;

    /**
     * Compares the faces found on the passport and the
     * uploaded selfie, which is then sent back to the client.
     *
     * @param passport The student's passport.
     * @param selfiePhoto The selfie of the student.
     * @return The validity- and the percentage of the faces matching.
     */
    @Operation(
        summary = "Compares the faces found on the passport and the selfie, and then sends it back to the client.",
        responses = {
            @ApiResponse(responseCode = "200", description = "The facial validation data.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FaceApiResponse.class))
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
    public ResponseEntity<FaceApiResponse> getSelfieValidationData(
            @RequestParam("passport") final MultipartFile passport,
            @RequestParam("selfiePhoto") final MultipartFile selfiePhoto
    ) {
        FaceApiResponse faceApiResponse =
                this.faceApiService.getValidityOfFaces(
                        passport, selfiePhoto
                );

        return new ResponseEntity<>(faceApiResponse, HttpStatus.OK);
    }
}
