package com.markvarga21.studentmanager.service.file;

import com.markvarga21.studentmanager.entity.StudentImage;
import com.markvarga21.studentmanager.util.StudentImageType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * The {@code FileUploadService} interface is used to
 * store the images in the database.
 */
@Service
public interface FileUploadService {
    /**
     * A method used to store
     * the images in the database.
     *
     * @param studentId The id of the student.
     * @param passportFile The passport file.
     * @param selfieFile The selfie file.
     * @return A feedback message.
     */
    String uploadFile(
            Long studentId,
            MultipartFile passportFile,
            MultipartFile selfieFile
    );

    /**
     * A method which is used to get
     * all the student images from the database.
     *
     * @param page The page number.
     * @param size The number of elements in a single page.
     * @return A page containing the student's images.
     */
    Page<StudentImage> getAllImages(Integer page, Integer size);

    /**
     * A method which is used to delete
     * the images from the database.
     *
     * @param studentId The id of the student.
     * @return An informational message.
     */
    String deleteImage(Long studentId);

    /**
     * A method which is used to get
     * the images for the given type and
     * student id.
     *
     * @param studentId The id of the student.
     * @param type The image type.
     * @return The image.
     */
    byte[] getImageForType(Long studentId, StudentImageType type);

    /**
     * A method which is used to change
     * the images for the given type and student.
     *
     * @param studentId The id of the student.
     * @param imageType The image type.
     * @param file The new image.
     * @return The message.
     */
    StudentImage changeImage(Long studentId, StudentImageType imageType, MultipartFile file);

    /**
     * A method which is used to get
     * the images for the given student.
     *
     * @param studentId The id of the student.
     * @return The images of the student.
     */
    StudentImage getStudentImageById(Long studentId);
}
