package com.markvarga21.usermanager.service.faceapi;

import com.markvarga21.usermanager.entity.FacialValidationData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * An interface for validating and comparing the faces on the
 * ID document/passport and the selfie itself.
 */
public interface FaceApiService {
    /**
     * Validates the faces on the ID card against the selfie.
     *
     * @param idPhoto the ID card file.
     * @param selfiePhoto the selfie file.
     */
    void facesAreMatching(MultipartFile idPhoto, MultipartFile selfiePhoto);

    /**
     * Compares the faces found on the passport and the
     * portrait, and then sends it back to the client.
     *
     * @param passport the user's passport.
     * @param selfiePhoto the portrait of the user.
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @return the validity and the percentage of the matching.
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
     * @return all the facial validation data.
     */
    List<FacialValidationData> fetchAllValidationData();

    /**
     * Deletes the facial validation data by ID.
     *
     * @param id the ID of the facial validation data.
     */
    void deleteFacialValidationData(Long id);
}
