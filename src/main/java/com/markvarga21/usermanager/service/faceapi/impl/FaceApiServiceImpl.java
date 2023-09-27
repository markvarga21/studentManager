package com.markvarga21.usermanager.service.faceapi.impl;

import com.markvarga21.usermanager.exception.InvalidIdDocumentException;
import com.markvarga21.usermanager.service.faceapi.FaceApiResponse;
import com.markvarga21.usermanager.service.faceapi.FaceApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * A service which uses Deepface to compare two faces.
 * It is then used for comparing the face on the
 * ID card/passport and the selfie which the user has uploaded.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FaceApiServiceImpl implements FaceApiService {
    /**
     * The endpoint of the Face API.
     */
    @Value("${face.api.endpoint}")
    private String faceApiUrl;
    /**
     * The rest template used to make API calls.
     */
    private final RestTemplate restTemplate;
    /**
     * A simple multiplier for converting floating point percentage
     * to decimal percentage.
     */
    public static final int PERCENT_MULTIPLIER = 100;

    /**
     * Compares two faces.
     *
     * @param idPhoto the ID photo of the user.
     * @param selfiePhoto a selfie of the user.
     */
    @Override
    public void facesAreMatching(
            final MultipartFile idPhoto,
            final MultipartFile selfiePhoto
    ) {
        if (idPhoto == null || selfiePhoto == null) {
            String message = "ID photo or selfie file missing!";
            log.error(message);
            throw new InvalidIdDocumentException(message);
        }
        log.info("Comparing faces...");
        try {
            FaceApiResponse faceApiResponse =
                    this.compareFaces(idPhoto, selfiePhoto);

            if (faceApiResponse == null) {
                String message = "Face api response is NULL!";
                log.error(message);
                throw new InvalidIdDocumentException(message);
            }

            log.info(String.format(
                "The probability that the faces are the same is %,.2f percent.",
                PERCENT_MULTIPLIER * faceApiResponse.getProba())
            );

            if (Boolean.FALSE.equals(faceApiResponse.getIsValid())) {
                String message =
                        "ID document photo is not matching with the selfie!";
                throw new InvalidIdDocumentException(message);
            }
        } catch (IOException exception) {
            String message = String.format(
                    "Something went wrong when comparing the photos: %s",
                    exception.getMessage()
            );
            log.error(message);
        }
    }

    private FaceApiResponse compareFaces(
            final MultipartFile passport,
            final MultipartFile selfiePhoto
    ) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        byte[] idPhotoBytes = passport.getBytes();
        body.add("idPhoto", new ByteArrayResource(idPhotoBytes) {
            @Override
            public String getFilename() {
                return passport.getOriginalFilename();
            }
        });
        byte[] selfiePhotoBytes = selfiePhoto.getBytes();
        body.add("selfiePhoto", new ByteArrayResource(selfiePhotoBytes) {
            @Override
            public String getFilename() {
                return selfiePhoto.getOriginalFilename();
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccessControlAllowOrigin("*");

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<FaceApiResponse> response = this.restTemplate
                .postForEntity(
                        this.faceApiUrl,
                        requestEntity,
                        FaceApiResponse.class
                );
        return response.getBody();
    }

    /**
     * Compares the faces found on the passport and the
     * portrait, and then sends it back to the client.
     *
     * @param passport the user's passport.
     * @param selfiePhoto the portrait of the user.
     * @return the validity and the percentage of the matching.
     */
    @Override
    public FaceApiResponse getValidityOfFaces(
            final MultipartFile passport,
            final MultipartFile selfiePhoto
    ) {
        try {
             return this.compareFaces(passport, selfiePhoto);
        } catch (IOException exception) {
            String message = String.format(
                    "Something went wrong when comparing the photos: %s",
                    exception.getMessage()
            );
            log.error(message);
        }
        return new FaceApiResponse();
    }
}
