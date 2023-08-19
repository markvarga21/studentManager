package com.markvarga21.usermanager.service.faceapi;

import org.springframework.web.multipart.MultipartFile;

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
}
