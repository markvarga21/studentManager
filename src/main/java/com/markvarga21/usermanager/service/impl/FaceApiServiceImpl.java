package com.markvarga21.usermanager.service.impl;

import com.markvarga21.usermanager.service.FaceApiService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * A service which uses Azure's Face API to compare two faces. It is then used for comparing the photo from the
 * ID card/passport and the selfie which the user has uploaded.
 */
@Component
public class FaceApiServiceImpl implements FaceApiService {
    /**
     * Compares two faces.
     *
     * @param idPhoto the identification photo of the user. Can be passport or ID document.
     * @param selfiePhoto the selfie which the user had taken for validation purposes.
     * @return {@code true} if the two faces match up, else {@code false}.
     */
    @Override
    public boolean compareFaces(MultipartFile idPhoto, MultipartFile selfiePhoto) {

        return false;
    }
}
