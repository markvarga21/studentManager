package com.markvarga21.usermanager.service.faceapi;

import com.markvarga21.usermanager.dto.FaceApiResponse;
import com.markvarga21.usermanager.dto.FacialValidationData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * An interface for validating and comparing the faces on the
 * passport and the selfie itself.
 */
public interface FaceApiService {
    /**
     * Validates the faces on the ID card against the selfie.
     *
     * @param passport The passport file.
     * @param selfiePhoto The selfie file.
     */
    void facesAreMatching(MultipartFile passport, MultipartFile selfiePhoto);

    /**
     * Compares the faces found on the passport and the
     * selfie, and then sends it back to the client.
     *
     * @param passport The student's passport.
     * @param selfiePhoto The selfie of the student.
     * @param firstName The first name of the student.
     * @param lastName The last name of the student.
     * @return The validity and the percentage of the matching.
     */
    FaceApiResponse getValidityOfFaces(
            MultipartFile passport,
            MultipartFile selfiePhoto,
            String firstName,
            String lastName
    );

    /**
     * Returns all the facial validation data.
     *
     * @return All the facial validation data.
     */
    List<FacialValidationData> fetchAllValidationData();

    /**
     * Deletes the facial validation data by ID.
     *
     * @param id The ID of the facial validation data.
     */
    void deleteFacialValidationData(Long id);

    /**
     * Returns the face ID for the given file.
     *
     * @param file The file to get the face ID for.
     * @return The face ID for the given file.
     */
    String getFaceIdForFile(MultipartFile file);

    /**
     * Deletes the facial data by first- and last name.
     *
     * @param firstName The first name of the student.
     * @param lastName The last name of the student.
     */
    void deleteFacialDataByFirstNameAndLastName(
            String firstName,
            String lastName
    );
}
