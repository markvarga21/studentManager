package com.markvarga21.studentmanager.controller;

import com.azure.core.annotation.QueryParam;
import com.markvarga21.studentmanager.entity.StudentImage;
import com.markvarga21.studentmanager.service.file.FileUploadService;
import com.markvarga21.studentmanager.util.StudentImageType;
import lombok.RequiredArgsConstructor;
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
public class FileUploadController {
    /**
     * The FileUploadService is used to manipulate
     * the image in the database.
     */
    private final FileUploadService fileUploadService;

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
     * @param passportNumber The students passport number.
     */
    @DeleteMapping("/{passportNumber}")
    public void deleteImage(
            @PathVariable("passportNumber") final String passportNumber
    ) {
        this.fileUploadService.deleteImage(passportNumber);
    }

    /**
     * The getImageForType method is used to get
     * the image for the specified type.
     *
     * @param passportNumber The students passport number.
     * @param imageType The type of image.
     * @return The image for the specified type.
     */
    @GetMapping("/{passportNumber}")
    public ResponseEntity<?> getImageForType(
            @PathVariable("passportNumber") final String passportNumber,
            @QueryParam("imageType") final StudentImageType imageType
            ) {
        byte[] image = this.fileUploadService
                .getImageForType(passportNumber, imageType);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    /**
     * The uploadImage method is used to upload
     * the image(s) to the database.
     *
     * @param passportNumber The students passport number.
     * @param passport The passport image.
     * @param selfie The selfie image.
     * @return A response entity.
     */
    @PostMapping("/upload/{passportNumber}")
    public ResponseEntity<?> uploadImage(
            @PathVariable("passportNumber") final String passportNumber,
            @RequestParam("passport") final MultipartFile passport,
            @RequestParam("selfie") final MultipartFile selfie
    ) {
        this.fileUploadService.uploadFile(passportNumber, passport, selfie);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}