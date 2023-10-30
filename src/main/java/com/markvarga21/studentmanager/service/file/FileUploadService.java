package com.markvarga21.studentmanager.service.file;

import com.markvarga21.studentmanager.entity.StudentImage;
import com.markvarga21.studentmanager.util.StudentImageType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * The FileUploadService interface is used to
 * store the image in the database.
 */
@Service
public interface FileUploadService {
    /**
     * The uploadFile method is used to store
     * the image in the database.
     *
     * @param passportNumber The students passport number.
     * @param passportFile The passport file.
     * @param selfieFile The selfie file.
     */
    void uploadFile(
            String passportNumber,
            MultipartFile passportFile,
            MultipartFile selfieFile
    );

    /**
     * The getAllImages method is used to get all
     * the images from the database.
     *
     * @return A list of all the images.
     */
    List<StudentImage> getAllImages();

    /**
     * The deleteImage method is used to delete
     * the image from the database.
     *
     * @param passportNumber The students passport number.
     */
    void deleteImage(String passportNumber);

    /**
     * The getImageForType method is used to get
     * the image for the given type.
     *
     * @param passportNumber The students passport number.
     * @param type The image type.
     * @return The image.
     */
    byte[] getImageForType(String passportNumber, StudentImageType type);
}
