package com.markvarga21.studentmanager.controller;

import com.azure.core.annotation.QueryParam;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.PassportValidationData;
import com.markvarga21.studentmanager.entity.StudentImage;
import com.markvarga21.studentmanager.service.StudentService;
import com.markvarga21.studentmanager.service.faceapi.FaceApiService;
import com.markvarga21.studentmanager.service.file.FileUploadService;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.service.validation.passport.PassportValidationService;
import com.markvarga21.studentmanager.StudentImageType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * The FileUploadController is used to manipulate the
 * image in the database.
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/files")
@Slf4j
@Tag(name = "File upload services", description = "The file upload related endpoints.")
public class FileUploadController {
    /**
     * The FileUploadService is used to manipulate
     * the image in the database.
     */
    private final FileUploadService fileUploadService;

    /**
     * The StudentService is used to manipulate
     * the student in the database.
     */
    private final StudentService studentService;

    /**
     * The FaceApiService is used to manipulate
     * face related data.
     */
    private final FaceApiService faceApiService;

    /**
     * The FormRecognizerService is used to
     * extract data from the passport.
     */
    private final FormRecognizerService formRecognizerService;

    /**
     * The PassportValidationService is used to
     * validate the passport.
     */
    private final PassportValidationService passportValidationService;

    /**
     * The getAllImages method is used to get all
     * the images from the database.
     *
     * @return A list of all the images.
     */
    @Operation(
            summary = "Retrieves all the images.",
            description = "Retrieves all the images of a student.")
    @GetMapping
    public List<StudentImage> getAllImages() {
        return this.fileUploadService.getAllImages();
    }

    /**
     * The deleteImage method is used to delete
     * the image from the database.
     *
     * @param studentId The id of the student.
     * @return A message which indicates whether the deletion
     * was successful or not.
     */
    @Operation(
            summary = "Deletes the image with the given student ID.",
            description = "Deletes the image with the given student ID."
    )
    @DeleteMapping("/{studentId}")
    public ResponseEntity<String> deleteImage(
            @PathVariable("studentId") final Long studentId
    ) {
        String message = this.fileUploadService.deleteImage(studentId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    /**
     * The getImageForType method is used to get
     * the image for the specified type.
     *
     * @param studentId The if of the student.
     * @param imageType The type of image.
     * @return The image for the specified type.
     */
    @Operation(
            summary = "Retrieves the image for the specified type.",
            description = "Retrieves the image for the specified type."
    )
    @GetMapping("/{studentId}")
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
     * The uploadImage method is used to upload
     * the image(s) to the database.
     *
     * @param studentId The id of the student.
     * @param passport The passport image.
     * @param selfie The selfie image.
     * @return A response entity.
     */
    @Operation(
            summary = "Uploads the image(s) to the database.",
            description = "Uploads the image(s) to the database."
    )
    @PostMapping("/upload/{studentId}")
    public ResponseEntity<String> uploadImage(
            @PathVariable("studentId") final Long studentId,
            @RequestParam("passport") final MultipartFile passport,
            @RequestParam("selfie") final MultipartFile selfie
    ) {
        String message = this.fileUploadService
                .uploadFile(studentId, passport, selfie);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * The changeImage method is used to change
     * the image(s) in the database.
     *
     * @param studentId The id of the student.
     * @param imageType The type of image.
     * @param file The file to be changed.
     * @return A response entity.
     */
    @Operation(
            summary = "Changes the image(s) in the database.",
            description = "Changes the image(s) in the database."
    )
    @PostMapping("/changeImage/{studentId}/{imageType}")
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
}
