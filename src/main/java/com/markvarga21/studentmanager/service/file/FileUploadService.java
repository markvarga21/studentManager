package com.markvarga21.studentmanager.service.file;

import com.markvarga21.studentmanager.entity.StudentImage;
import com.markvarga21.studentmanager.util.StudentImageType;
import org.springframework.data.domain.Page;
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
     * @param studentId The id of the student.
     * @param passportFile The passport file.
     * @param selfieFile The selfie file.
     * @return The message.
     */
    String uploadFile(
            Long studentId,
            MultipartFile passportFile,
            MultipartFile selfieFile
    );

    /**
     * The getAllImages method is used to get
     * all the student images from the database.
     *
     * @param page The page number.
     * @param size The number of elements in a page.
     * @return The images.
     */
    Page<StudentImage> getAllImages(Integer page, Integer size);

    /**
     * The deleteImage method is used to delete
     * the image from the database.
     *
     * @param studentId The id of the student.
     * @return The message.
     */
    String deleteImage(Long studentId);

    /**
     * The getImageForType method is used to get
     * the image for the given type.
     *
     * @param studentId The id of the student.
     * @param type The image type.
     * @return The image.
     */
    byte[] getImageForType(Long studentId, StudentImageType type);

    /**
     * The changeImage method is used to change
     * the image for the given type.
     *
     * @param studentId The id of the student.
     * @param imageType The image type.
     * @param file The new image.
     * @return The message.
     */
    StudentImage changeImage(Long studentId, StudentImageType imageType, MultipartFile file);

    /**
     * The getStudentImageById method is used to get
     * the image for the given student.
     *
     * @param studentId The id of the student.
     * @return The images of the student.
     */
    StudentImage getStudentImageById(Long studentId);
}
