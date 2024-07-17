package com.markvarga21.studentmanager.service.file.impl;

import com.markvarga21.studentmanager.entity.StudentImage;
import com.markvarga21.studentmanager.exception.InvalidDocumentException;
import com.markvarga21.studentmanager.exception.InvalidImageTypeException;
import com.markvarga21.studentmanager.exception.InvalidStudentException;
import com.markvarga21.studentmanager.exception.OperationType;
import com.markvarga21.studentmanager.exception.StudentNotFoundException;
import com.markvarga21.studentmanager.repository.StudentImageRepository;
import com.markvarga21.studentmanager.service.file.FileUploadService;
import com.markvarga21.studentmanager.util.ImageCompressor;
import com.markvarga21.studentmanager.util.StudentImageType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

        if (this.studentImageRepository.findById(studentId).isPresent()) {
            String message = String.format(
                    "Student with ID '%s' already has images",
                    studentId
            );
            log.error(message);
            throw new InvalidStudentException(message);
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
     * The getAllImages method is used to get
     * all the student images from the database.
     *
     * @param page The page number.
     * @param size The number of elements in a page.
     * @return The images.
     */
    @Override
    public Page<StudentImage> getAllImages(
            final Integer page,
            final Integer size
    ) {
        return this.studentImageRepository.findAll(
                PageRequest.of(page, size)
        );
    }

    /**
     * A method is used to delete
     * the images from the database.
     *
     * @param studentId The id of the student.
     */
    @Override
    @Transactional
    @CacheEvict(value = "studentImage", key = "#studentId")
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
                    "Student with the ID '%s' does not exist",
                    studentId
            ));
            throw new StudentNotFoundException(
                    String.format(
                            "Student with ID '%s' doesn't exist",
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
     * @return The updated student image group.
     */
    @Override
    @CachePut(value = "studentImage", key = "#studentId")
    public StudentImage changeImage(
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
                return this.studentImageRepository.save(studentImage);
            }
            case PASSPORT -> {
                log.info("Changing passport image for student with ID: {}", studentId);
                studentImage.setPassportImage(ImageCompressor.compressImage(file));
                return this.studentImageRepository.save(studentImage);
            }
            default -> {
                String message = "Image type not provided or not valid!\nValid image types are: PASSPORT, SELFIE";
                log.error(message);
                throw new InvalidImageTypeException(message);
            }
        }
    }

    /**
     * The getStudentImageById method is used to get
     * the image for the given student ID.
     *
     * @param studentId The id of the student.
     * @return The student's images.
     */
    @Override
    @Cacheable(value = "studentImage", key = "#studentId")
    public StudentImage getStudentImageById(final Long studentId) {
        log.info("Invoking database...");
        Optional<StudentImage> studentImageOptional = this.studentImageRepository
                .findById(studentId);
        if (studentImageOptional.isEmpty()) {
            String message = String.format(
                    "Student with the ID '%s' does not exist",
                    studentId
            );
            log.error(message);
            throw new StudentNotFoundException(
                    message,
                    OperationType.READ
            );
        }
        return studentImageOptional.get();
    }
}
