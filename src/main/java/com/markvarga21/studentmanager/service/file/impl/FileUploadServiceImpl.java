package com.markvarga21.studentmanager.service.file.impl;

import com.markvarga21.studentmanager.entity.StudentImage;
import com.markvarga21.studentmanager.exception.InvalidDocumentException;
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
        try {
            StudentImage studentImage = StudentImage.builder()
                    .passportNumber(passportNumber)
                    .passportImage(passportImage.getBytes())
                    .selfieImage(selfieImage.getBytes())
                    .build();
            this.studentImageRepository.save(studentImage);
        } catch (IOException e) {
            String message = String.format(
                    "Error occurred while uploading image for student with passport number: %s",
                    passportNumber
            );
            log.error(message);
            throw new InvalidDocumentException(message);
        }
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
        this.studentImageRepository.deleteStudentImagesByPassportNumber(
                passportNumber
        );
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
}
