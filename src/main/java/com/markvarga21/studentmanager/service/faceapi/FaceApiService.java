package com.markvarga21.studentmanager.service.faceapi;

import com.markvarga21.studentmanager.dto.FaceApiResponse;
import org.springframework.web.multipart.MultipartFile;


/**
 * An interface for validating and comparing the faces on the
 * passport and the selfie itself.
 */
public interface FaceApiService {
    /**
     * Compares two faces.
     *
     * @param passport The passport of the student.
     * @param selfiePhoto A selfie of the student.
     */
    void facesAreMatching(
            MultipartFile passport,
            MultipartFile selfiePhoto
    );

    /**
     * Compares the faces found on the passport and the
     * selfie, and then sends it back to the client.
     *
     * @param passport The student's passport.
     * @param selfiePhoto The selfie of the student.
     * @return The validity and the percentage of the matching.
     */
    FaceApiResponse getValidityOfFaces(
            MultipartFile passport,
            MultipartFile selfiePhoto
    );

    /**
     * Returns the face ID for the given file.
     *
     * @param file The file to get the face ID for.
     * @return The face ID for the given file.
     */
    String getFaceIdForFile(MultipartFile file);

    /**
     * Validates the faces for the given passport number.
     *
     * @param passportNumber The passport number.
     */
    void validateFacesForPassportNumber(String passportNumber);

    /**
     * Deletes the face with the given passport number.
     *
     * @param passportNumber The passport number.
     */
    void deleteFace(String passportNumber);
}
