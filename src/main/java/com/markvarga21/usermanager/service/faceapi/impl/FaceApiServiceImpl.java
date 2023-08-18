package com.markvarga21.usermanager.service.faceapi.impl;

import com.markvarga21.usermanager.exception.InvalidIdDocumentException;
import com.markvarga21.usermanager.service.faceapi.FaceApiResponse;
import com.markvarga21.usermanager.service.faceapi.FaceApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * A service which uses Deepface to compare two faces.
 * It is then used for comparing the photo from the
 * ID card/passport and the selfie which the user has uploaded.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FaceApiServiceImpl implements FaceApiService {
    @Value("${face.api.endpoint}")
    private String faceApiUrl;
    private final RestTemplate restTemplate;

    /**
     * Compares two faces.
     *
     * @param idPhoto the identification photo of the user. Can be passport or ID document.
     * @param selfiePhoto the selfie which the user had taken for validation purposes.
     */
    @Override
    public void facesAreMatching(MultipartFile idPhoto, MultipartFile selfiePhoto) {
        log.info("Comparing faces...");
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            body.add("idPhoto", new ByteArrayResource(idPhoto.getBytes()) {
                @Override
                public String getFilename() {
                    return idPhoto.getOriginalFilename();
                }
            });
            body.add("selfiePhoto", new ByteArrayResource(selfiePhoto.getBytes()) {
                @Override
                public String getFilename() {
                    return selfiePhoto.getOriginalFilename();
                }
            });

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<FaceApiResponse> response = this.restTemplate.postForEntity(this.faceApiUrl, requestEntity, FaceApiResponse.class);
            FaceApiResponse faceApiResponse = response.getBody();

            if (faceApiResponse == null) {
                String message = "Face api response is NULL!";
                log.error(message);
                throw new InvalidIdDocumentException(message);
            }

            log.info(String.format(
                    "The probability that the faces are the same is %,.2f percent.",
                    100*faceApiResponse.getProba())
            );

            if (Boolean.FALSE.equals(faceApiResponse.getIsValid())) {
                String message = "ID document photo is not matching with the selfie!";
                throw new InvalidIdDocumentException(message);
            }
        } catch (IOException exception) {
            String message = String.format("Something went wrong when comparing the photos: %s", exception.getMessage());
            log.error(message);
        }
    }
}
