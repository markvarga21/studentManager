package com.markvarga21.studentmanager.controller;

import com.azure.core.annotation.QueryParam;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.PassportValidationData;
import com.markvarga21.studentmanager.entity.StudentImage;
import com.markvarga21.studentmanager.exception.util.ApiError;
import com.markvarga21.studentmanager.exception.util.AuthError;
import com.markvarga21.studentmanager.service.StudentService;
import com.markvarga21.studentmanager.service.faceapi.FaceApiService;
import com.markvarga21.studentmanager.service.file.FileUploadService;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.service.validation.passport.PassportValidationService;
import com.markvarga21.studentmanager.util.StudentImageType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * A controller class which is used to manipulate the
 * student's images in the database.
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/files")
@Slf4j
@Tag(
    name = "File upload services",
    description = "The file upload related endpoints."
)
public class FileUploadController {
    /**
     * A service class used to manipulate
     * the images in the database.
     */
    private final FileUploadService fileUploadService;

    /**
     * A service class used to manipulate
     * students in the database.
     */
    private final StudentService studentService;

    /**
     * A service class used to manipulate
     * face related operations.
     */
    private final FaceApiService faceApiService;

    /**
     * A service class used to
     * extract data from the passport.
     */
    private final FormRecognizerService formRecognizerService;

    /**
     * A service class used to
     * validate the passport.
     */
    private final PassportValidationService passportValidationService;

    /**
     * Fetches all the images from the database.
     *
     * @param page The page number.
     * @param size The number of elements in a single page.
     * @return A page containing the student's images.
     */
    @Operation(
        summary = "Retrieves all the images.",
        responses = {
            @ApiResponse(responseCode = "200", description = "A page of student images.", content = {
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
    public Page<StudentImage> getAllImages(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "10") final Integer size
    ) {
        return this.fileUploadService.getAllImages(page, size);
    }

    /**
     * Deletes the student's passport- and portrait image
     * from the database using their id's.
     *
     * @param studentId The id of the student.
     * @return A message which indicates whether the deletion
     * was successful or not.
     */
    @Operation(
        summary = "Deletes the image with the given student ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "A status message of the deletion.", content = {
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
    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteImage(
            @PathVariable("studentId") final Long studentId
    ) {
        String message = this.fileUploadService.deleteImage(studentId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    /**
     * Retrieves a single image of the give type for
     * a student identified by it's id.
     *
     * @param studentId The id of the student.
     * @param imageType The type of the image, portrait or passport.
     * @return The student's image of the specified type.
     */
    @Operation(
        summary = "Retrieves the image of a specified type.",
        responses = {
            @ApiResponse(responseCode = "200", description = "The fetched image for the given type.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Byte.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
    @GetMapping("/{studentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<byte[]> getImageForType(
            @PathVariable("studentId") final Long studentId,
            @QueryParam("imageType") final StudentImageType imageType
            ) {
        byte[] image = this.fileUploadService
                .getImageForType(studentId, imageType);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    /**
     * Uploads both images for the given student identified
     * by their id.
     *
     * @param studentId The id of the student.
     * @param passport The passport image.
     * @param selfie The selfie image.
     * @return A response entity.
     */
    @Operation(
        summary = "Uploads the image(s) to the database.",
        responses = {
            @ApiResponse(responseCode = "200", description = "A status message about the image upload.", content = {
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
    @PostMapping("/upload/{studentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> uploadImage(
            @PathVariable("studentId") final Long studentId,
            @RequestParam("passport") final MultipartFile passport,
            @RequestParam("selfie") final MultipartFile selfie
    ) {
        log.info("Passport: " + passport.getOriginalFilename());
        log.info("Selfie: " + selfie.getOriginalFilename());
        String message = this.fileUploadService
                .uploadFile(studentId, passport, selfie);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Modifies the given type of image of the student.
     *
     * @param studentId The id of the student.
     * @param imageType The type of image.
     * @param file The file to be changed.
     * @return A response entity.
     */
    @Operation(
        summary = "Changes the image(s) in the database.",
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
    @PostMapping("/changeImage/{studentId}/{imageType}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> changeImage(
            @PathVariable("studentId") final Long studentId,
            @PathVariable("imageType") final StudentImageType imageType,
            @RequestParam("file") final MultipartFile file
    ) {
        StudentDto student = this.studentService.getStudentById(studentId);
        this.fileUploadService.changeImage(studentId, imageType, file);
        String updateMessage = this.studentService
                .setValidity(studentId, false);
        log.info(updateMessage);
        final String passportNumber = student.getPassportNumber();
        String message = "";
        switch (imageType) {
            case PASSPORT -> {
                this.passportValidationService
                        .deletePassportValidationData(passportNumber);
                // Extract new validation data
                StudentDto studentDto = this.formRecognizerService
                        .extractDataFromPassport(file);
                // Save new validation data
                PassportValidationData data = PassportValidationData
                        .createPassportValidationDataForStudent(studentDto);
                this.passportValidationService
                        .createPassportValidationData(data);
                message = String.format(
                        "Passport image changed successfully for user '%s'",
                        studentId
                );
            }
            case SELFIE -> {
                this.faceApiService.deleteFace(passportNumber);
                message = String.format(
                        "Selfie image changed successfully for user '%s'",
                        studentId
                );
            }
            default -> log.error("Invalid image type");
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Fetches both the pictures of the student by
     * their id.
     *
     * @param studentId The id of the student.
     * @return The images for the specified student id.
     */
    @Operation(
        summary = "Fetches both images for a student.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Both images for a student.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StudentImage.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
    @GetMapping("/combined/{studentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<StudentImage> getImagesForStudentId(
            @PathVariable final Long studentId
    ) {
        StudentImage studentImage = this.fileUploadService
                .getStudentImageById(studentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentImage);
    }
}
