package com.markvarga21.studentmanager.service.file.impl;

import com.markvarga21.studentmanager.entity.StudentImage;
import com.markvarga21.studentmanager.exception.InvalidDocumentException;
import com.markvarga21.studentmanager.exception.InvalidImageTypeException;
import com.markvarga21.studentmanager.exception.OperationType;
import com.markvarga21.studentmanager.exception.StudentNotFoundException;
import com.markvarga21.studentmanager.repository.StudentImageRepository;
import com.markvarga21.studentmanager.service.file.FileUploadService;
import com.markvarga21.studentmanager.util.ImageCompressor;
import com.markvarga21.studentmanager.util.StudentImageType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * The FileUploadServiceImpl class is used to store and manipulate the
 * images of a student.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {
    /**
     * The StudentImageRepository is used to store
     * the image in the database.
     */
    private final StudentImageRepository studentImageRepository;

    /**
     * The uploadFile method is used to store the
     * image in the database.
     *
     * @param studentId The id of the student.
     * @param passportImage The passport file.
     * @param selfieImage The selfie file.
     */
    @Override
    @Transactional
    public String uploadFile(
            final Long studentId,
            final MultipartFile passportImage,
            final MultipartFile selfieImage
    ) {
        if (passportImage.isEmpty()) {
            String message = "Passport image is empty";
            log.error(message);
            throw new InvalidDocumentException(message);
        }
        if (selfieImage.isEmpty()) {
            String message = "Selfie image is empty";
            log.error(message);
            throw new InvalidDocumentException(message);
        }

        StudentImage studentImage = StudentImage.builder()
                .studentId(studentId)
                .passportImage(ImageCompressor.compressImage(passportImage))
                .selfieImage(ImageCompressor.compressImage(selfieImage))
                .build();
        log.info("Saving images for studentId " + studentId);
        this.studentImageRepository.save(studentImage);
        return String.format("Images saved successfully for user '%s'", studentId);
    }

    /**
     * The getAllImages method is used to get all
     * the images from the database.
     *
     * @return A list of all the images.
     */
    @Override
    public List<StudentImage> getAllImages() {
        return this.studentImageRepository.findAll();
    }

    /**
     * A method is used to delete
     * the images from the database.
     *
     * @param studentId The id of the student.
     */
    @Override
    @Transactional
    public String deleteImage(
            final Long studentId
    ) {
        Optional<StudentImage> studentImageOptional =
                this.studentImageRepository.findById(studentId);

        if (studentImageOptional.isPresent()) {
            this.studentImageRepository.deleteStudentImageByStudentId(
                    studentId
            );
            return String.format(
                    "Images deleted successfully for user '%s'",
                    studentId
            );
        } else {
            log.error(String.format(
                    "Student with ID '%s' does not exist",
                    studentId
            ));
            throw new StudentNotFoundException(
                    String.format(
                            "Student with ID '%s' does not exist",
                            studentId
                    ),
                    OperationType.DELETE
            );
        }
    }

    /**
     * The getImageForType method is used to get
     * the image for the given type.
     *
     * @param studentId The id of the student.
     * @param type The image type.
     * @return The image.
     */
    @Override
    public byte[] getImageForType(
            final Long studentId,
            final StudentImageType type
    ) {
        if (type == null || !type.equals(StudentImageType.PASSPORT) && !type.equals(StudentImageType.SELFIE)) {
            String message = "Image type not provided or not valid!\nValid image types are: PASSPORT, SELFIE";
            log.error(message);
            throw new InvalidImageTypeException(message);
        }
        Optional<StudentImage> studentImageOptional =
                this.studentImageRepository.findById(studentId);
        if (studentImageOptional.isEmpty()) {
            String message = String.format(
                    "Student with ID '%s' does not exist",
                    studentId
            );
            throw new StudentNotFoundException(message, OperationType.READ);
        }
        return switch (type) {
            case PASSPORT -> studentImageOptional
                                    .get()
                                    .getPassportImage();
            case SELFIE -> studentImageOptional
                                    .get()
                                    .getSelfieImage();
            default -> throw new InvalidImageTypeException("Invalid image type provided");
        };
    }

    /**
     * The changeImage method is used to change
     * the image for the given type.
     *
     * @param studentId The id of the student.
     * @param imageType The image type.
     * @param file The new image.
     */
    @Override
    public String changeImage(
            final Long studentId,
            final StudentImageType imageType,
            final MultipartFile file
    ) {
        if (file == null) {
            String message = "File not provided!";
            log.error(message);
            throw new InvalidDocumentException(message);
        }

        StudentImage studentImage = this.studentImageRepository
                .findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        String.format(
                                "Student with id '%s' does not exist",
                                studentId
                        ),
                        OperationType.UPDATE
                ));

        switch (imageType) {
            case SELFIE -> {
                log.info("Changing selfie image for student with ID: {}", studentId);
                studentImage.setSelfieImage(ImageCompressor.compressImage(file));
                this.studentImageRepository.save(studentImage);
                return String.format("Selfie image changed successfully for user '%s'", studentId);
            }
            case PASSPORT -> {
                log.info("Changing passport image for student with ID: {}", studentId);
                studentImage.setPassportImage(ImageCompressor.compressImage(file));
                this.studentImageRepository.save(studentImage);
                return String.format("Passport image changed successfully for user '%s'", studentId);
            }
            default -> {
                String message = "Image type not provided or not valid!\nValid image types are: PASSPORT, SELFIE";
                log.error(message);
                throw new InvalidImageTypeException(message);
            }
        }
    }
}
