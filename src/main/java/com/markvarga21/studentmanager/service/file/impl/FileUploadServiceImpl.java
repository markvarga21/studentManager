package com.markvarga21.studentmanager.service.file.impl;

import com.markvarga21.studentmanager.entity.StudentImage;
import com.markvarga21.studentmanager.exception.InvalidDocumentException;
import com.markvarga21.studentmanager.exception.InvalidImageTypeException;
import com.markvarga21.studentmanager.exception.InvalidPassportException;
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

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
     * @param passportNumber The students passport number.
     * @param passportImage The passport file.
     * @param selfieImage The selfie file.
     */
    @Override
    @Transactional
    public void uploadFile(
            final String passportNumber,
            final MultipartFile passportImage,
            final MultipartFile selfieImage
    ) {
        if (passportImage.isEmpty() || passportNumber == null) {
            String message = "Passport number not provided!";
            log.error(message);
            throw new InvalidDocumentException(message);
        }
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
                .passportNumber(passportNumber)
                .passportImage(ImageCompressor.compressImage(passportImage))
                .selfieImage(ImageCompressor.compressImage(selfieImage))
                .build();
        this.studentImageRepository.save(studentImage);
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
     * @param passportNumber The students passport number.
     */
    @Override
    @Transactional
    public void deleteImage(
            final String passportNumber
    ) {
        if (passportNumber == null) {
            String message = "Passport number not provided!";
            log.error(message);
            throw new InvalidPassportException(message);
        }
        Optional<StudentImage> studentImageOptional =
                this.studentImageRepository.findById(passportNumber);

        if (studentImageOptional.isPresent()) {
            this.studentImageRepository.deleteStudentImagesByPassportNumber(
                    passportNumber
            );
        } else {
            log.error(String.format(
                    "Student with passport number: %s does not exist",
                    passportNumber
            ));
        }
    }

    /**
     * The getImageForType method is used to get
     * the image for the given type.
     *
     * @param passportNumber The students passport number.
     * @param type The image type.
     * @return The image.
     */
    @Override
    public byte[] getImageForType(
            final String passportNumber,
            final StudentImageType type
    ) {
        if (passportNumber == null) {
            String message = "Passport number not provided!";
            log.error(message);
            throw new InvalidPassportException(message);
        }
        if (type == null || !type.equals(StudentImageType.PASSPORT) && !type.equals(StudentImageType.SELFIE)) {
            String message = "Image type not provided or not valid!\nValid image types are: PASSPORT, SELFIE";
            log.error(message);
            throw new InvalidImageTypeException(message);
        }
        Optional<StudentImage> studentImageOptional =
                this.studentImageRepository.findById(passportNumber);
        if (studentImageOptional.isEmpty()) {
            String message = String.format(
                    "Student with passport number: %s does not exist",
                    passportNumber
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
        };
    }

    /**
     * The changeImage method is used to change
     * the image for the given type.
     *
     * @param passportNumber The students passport number.
     * @param imageType The image type.
     * @param file The new image.
     */
    @Override
    public void changeImage(
            final String passportNumber,
            final StudentImageType imageType,
            final MultipartFile file
    ) {
        if (file == null) {
            String message = "File not provided!";
            log.error(message);
            throw new InvalidDocumentException(message);
        }

        StudentImage studentImage = this.studentImageRepository
                .findById(passportNumber)
                .orElseThrow(() -> new StudentNotFoundException(
                        String.format(
                                "Student with passport number: %s does not exist",
                                passportNumber
                        ),
                        OperationType.UPDATE
                ));

        switch (imageType) {
            case SELFIE -> {
                log.info("Changing selfie image for student with passport number: {}", passportNumber);
                studentImage.setSelfieImage(ImageCompressor.compressImage(file));
                this.studentImageRepository.save(studentImage);
            }
            case PASSPORT -> {
                log.info("Changing passport image for student with passport number: {}", passportNumber);
                studentImage.setPassportImage(ImageCompressor.compressImage(file));
                this.studentImageRepository.save(studentImage);

            }
            default -> {
                String message = "Image type not provided or not valid!\nValid image types are: PASSPORT, SELFIE";
                log.error(message);
                throw new InvalidImageTypeException(message);
            }
        }
    }
}
