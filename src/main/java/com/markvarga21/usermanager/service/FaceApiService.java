package com.markvarga21.usermanager.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * An interface which contains methods used by the Face API.
 */
public interface FaceApiService {
    /**
     * Compares two faces.
     *
     * @param idPhoto the ID card or passport uploaded by the user.
     * @param selfiePhoto the selfie of the user.
     * @return {@code true} if the two faces are the same, else {@code false}.
     */
    boolean compareFaces(MultipartFile idPhoto, MultipartFile selfiePhoto);
}
