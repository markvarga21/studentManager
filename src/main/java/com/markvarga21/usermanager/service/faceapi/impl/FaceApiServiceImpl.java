package com.markvarga21.usermanager.service.faceapi.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.markvarga21.usermanager.config.ApplicationConfiguration;
import com.markvarga21.usermanager.dto.FaceDetectionResponse;
import com.markvarga21.usermanager.dto.FacialValidationData;
import com.markvarga21.usermanager.exception.FaceValidationDataNotFoundException;
import com.markvarga21.usermanager.exception.InvalidIdDocumentException;
import com.markvarga21.usermanager.exception.InvalidFacesException;
import com.markvarga21.usermanager.repository.FacialValidationDataRepository;
import com.markvarga21.usermanager.dto.FaceApiResponse;
import com.markvarga21.usermanager.service.faceapi.FaceApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Value("${knopp.services.endpoint}")
    private String faceApiUrl;

    /**
     * The key of the Face API.
     */
    @Value("${knopp.services.key}")
    private String faceApiKey;
    /**
     * The rest template used to make API calls.
     */
    private final RestTemplate restTemplate;

    /**
     * A repository which is used to access facial validation data.
     */
    private final FacialValidationDataRepository facialValidationDataRepository;

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
                PERCENT_MULTIPLIER * faceApiResponse.getConfidence())
            );

            if (Boolean.FALSE.equals(faceApiResponse.getIsIdentical())) {
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

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setAccessControlAllowOrigin("*");
        headers.set(
                ApplicationConfiguration.AZURE_API_KEY_HEADER,
                this.faceApiKey
        );
        return headers;
    }

    /**
     * Returns the face ID for the given file.
     *
     * @param file the file to be processed.
     * @return the face ID for the given file.
     */
    @Override
    public String getFaceIdForFile(final MultipartFile file) {
        try {
            String faceApiDetectionEndpoint = String.format(
                    "%sface/v1.0/detect?returnFaceId=true&recognitionModel=recognition_04&faceIdTimeToLive=300",
                    this.faceApiUrl
            );

            byte[] fileBytes = file.getBytes();
            HttpHeaders headers = this.getHeaders();
            HttpEntity<byte[]> requestEntity =
                    new HttpEntity<>(fileBytes, headers);
            ResponseEntity<String> response = this.restTemplate
                    .postForEntity(
                            faceApiDetectionEndpoint,
                            requestEntity,
                            String.class
                    );
            String detectionString = response.getBody();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<FaceDetectionResponse>>() { }.getType();

            ArrayList<FaceDetectionResponse> faceDetectionResponses = gson.fromJson(
                    detectionString,
                    listType
            );
            faceDetectionResponses.sort((face1, face2) -> {
                Integer area1 = face1.getFaceRectangle().getArea();
                Integer area2 = face2.getFaceRectangle().getArea();

                return area2.compareTo(area1);
            });

            return faceDetectionResponses.get(0).getFaceId();
        } catch (IOException exception) {
            String message = String.format(
                    "Something went wrong when extracting face ID: %s",
                    exception.getMessage()
            );
            log.error(message);
        }
        return "Face ID not present!";
    }

    private FaceApiResponse compareFaces(
            final MultipartFile passport,
            final MultipartFile selfiePhoto
    ) throws IOException {
        String passportFaceId = this.getFaceIdForFile(passport);
        String selfieFaceId = this.getFaceIdForFile(selfiePhoto);

        String faceApiVerificationEndpoint = String.format(
                "%sface/v1.0/verify",
                this.faceApiUrl
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(
                ApplicationConfiguration.AZURE_API_KEY_HEADER,
                this.faceApiKey
        );
        headers.setAccessControlAllowOrigin("*");

        String jsonString = String.format(
                "{\"faceId1\":\"%s\",\"faceId2\":\"%s\"}",
                passportFaceId,
                selfieFaceId
        );

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

        ResponseEntity<FaceApiResponse> response = this.restTemplate
                .postForEntity(
                        faceApiVerificationEndpoint,
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
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @return the validity and the percentage of the matching.
     */
    @Override
    public FaceApiResponse getValidityOfFaces(
            final MultipartFile passport,
            final MultipartFile selfiePhoto,
            final String firstName,
            final String lastName
    ) {
        Optional<FacialValidationData> facialValidationDataOptional =
                this.isUserPresentInFacialDatabase(firstName, lastName);
        if (facialValidationDataOptional.isPresent()) {
            log.info("User is present in the facial database.");
            return new FaceApiResponse(
                    true,
                    facialValidationDataOptional
                            .get()
                            .getProbabilityOfMatching()
            );
        } else {
            try {
                FaceApiResponse faceApiResponse =
                        this.compareFaces(passport, selfiePhoto);

                FacialValidationData facialValidationData = FacialValidationData
                        .builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .probabilityOfMatching(faceApiResponse.getConfidence())
                        .build();
                if (faceApiResponse.getIsIdentical()) {
                    this.facialValidationDataRepository.save(facialValidationData);
                }

                return faceApiResponse;
            } catch (IOException exception) {
                String message = String.format(
                        "Something went wrong when comparing the photos: %s",
                        exception.getMessage()
                );
                log.error(message);
                throw new InvalidFacesException(message);
            }
        }
    }

    /**
     * Checks if the user is present in the facial database.
     *
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @return an optional facial validation data.
     */
    private Optional<FacialValidationData> isUserPresentInFacialDatabase(
            final String firstName,
            final String lastName
    ) {
        return this.facialValidationDataRepository
                .findFacialValidationDataByFirstNameAndLastName(
                        firstName,
                        lastName
                );
    }

    /**
     * Deletes the facial data by first- and last name.
     *
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     */
    @Override
    public void deleteFacialDataByFirstNameAndLastName(
            final String firstName,
            final String lastName
    ) {
        Optional<FacialValidationData> optionalFacialValidationData
                = this.facialValidationDataRepository
                    .findFacialValidationDataByFirstNameAndLastName(
                        firstName,
                        lastName
                );
        if (optionalFacialValidationData.isEmpty()) {
            String message = String.format(
                    "Facial validation data for '%s %s' not found!",
                    firstName,
                    lastName
            );
            throw new FaceValidationDataNotFoundException(message);
        }
        this.facialValidationDataRepository
                .delete(optionalFacialValidationData.get());
    }

    /**
     * Returns all the facial validation data.
     *
     * @return all the facial validation data.
     */
    @Override
    public List<FacialValidationData> fetchAllValidationData() {
        return this.facialValidationDataRepository.findAll();
    }

    /**
     * Deletes the facial validation data by ID.
     *
     * @param id the ID of the facial validation data.
     */
    @Override
    public void deleteFacialValidationData(final Long id) {
        Optional<FacialValidationData> facialValidationDataOptional =
                this.facialValidationDataRepository.findById(id);
        if (facialValidationDataOptional.isPresent()) {
            this.facialValidationDataRepository.deleteById(id);
            return;
        }
        String message = String.format(
                "Facial validation data with ID %d not found!",
                id
        );
        throw new FaceValidationDataNotFoundException(message);
    }
}
