package com.markvarga21.studentmanager.controller;

import com.azure.core.annotation.QueryParam;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.PassportValidationData;
import com.markvarga21.studentmanager.entity.Student;
import com.markvarga21.studentmanager.entity.StudentImage;
import com.markvarga21.studentmanager.service.StudentService;
import com.markvarga21.studentmanager.service.faceapi.FaceApiService;
import com.markvarga21.studentmanager.service.file.FileUploadService;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.service.validation.passport.PassportValidationService;
import com.markvarga21.studentmanager.util.StudentImageType;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping
    public List<StudentImage> getAllImages() {
        return this.fileUploadService.getAllImages();
    }

    /**
     * The deleteImage method is used to delete
     * the image from the database.
     *
     * @param studentId The id of the student.
     */
    @DeleteMapping("/{studentId}")
    public void deleteImage(
            @PathVariable("studentId") final Long studentId
    ) {
        this.fileUploadService.deleteImage(studentId);
    }

    /**
     * The getImageForType method is used to get
     * the image for the specified type.
     *
     * @param studentId The if of the student.
     * @param imageType The type of image.
     * @return The image for the specified type.
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<?> getImageForType(
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
    @PostMapping("/upload/{studentId}")
    public ResponseEntity<?> uploadImage(
            @PathVariable("studentId") final Long studentId,
            @RequestParam("passport") final MultipartFile passport,
            @RequestParam("selfie") final MultipartFile selfie
    ) {
        this.fileUploadService.uploadFile(studentId, passport, selfie);
        return ResponseEntity.status(HttpStatus.OK).build();
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
    @PostMapping("/changeImage/{studentId}/{imageType}")
    public ResponseEntity<?> changeImage(
            @PathVariable("studentId") final Long studentId,
            @PathVariable("imageType") final StudentImageType imageType,
            @RequestParam("file") final MultipartFile file
    ) {
        StudentDto student = this.studentService.getStudentById(studentId);
        this.fileUploadService.changeImage(studentId, imageType, file);
        this.studentService.setValidity(studentId, false);
        final String passportNumber = student.getPassportNumber();
        switch (imageType) {
            case PASSPORT -> {
                this.passportValidationService.deletePassportValidationData(passportNumber);
                // Extract new validation data
                StudentDto studentDto = this.formRecognizerService
                        .extractDataFromPassport(file);
                // Save new validation data
                PassportValidationData data = PassportValidationData
                        .createPassportValidationDataForStudent(studentDto);
                this.passportValidationService.createPassportValidationData(data);
            }
            case SELFIE -> this.faceApiService.deleteFace(passportNumber);
            default -> log.error("Invalid image type");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
