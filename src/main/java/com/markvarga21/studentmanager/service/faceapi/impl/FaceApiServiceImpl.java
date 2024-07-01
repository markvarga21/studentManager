package com.markvarga21.studentmanager.service.faceapi.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.markvarga21.studentmanager.config.ApplicationConfiguration;
import com.markvarga21.studentmanager.dto.FaceDetectionResponse;
import com.markvarga21.studentmanager.entity.FacialValidationData;
import com.markvarga21.studentmanager.exception.InvalidPassportException;
import com.markvarga21.studentmanager.dto.FaceApiResponse;
import com.markvarga21.studentmanager.service.faceapi.FaceApiService;
import com.markvarga21.studentmanager.service.file.FileUploadService;
import com.markvarga21.studentmanager.service.validation.face.FacialValidationService;
import com.markvarga21.studentmanager.util.Generated;
import com.markvarga21.studentmanager.util.StudentImageType;
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

/**
 * A service which uses Azure's Face API to compare two faces.
 * It is then used for comparing the face on the
 * passport and the students selfie which has been uploaded.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Generated
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
     * The file upload service.
     */
    private final FileUploadService fileUploadService;

    /**
     * The facial validation service.
     */
    private final FacialValidationService facialValidationService;

    /**
     * A simple multiplier for converting floating point percentage
     * to decimal percentage.
     */
    public static final int PERCENT_MULTIPLIER = 100;

    /**
     * Compares two faces.
     *
     * @param passport The passport photo of the student.
     * @param selfiePhoto A selfie of the student.
     */
    @Override
    public void facesAreMatching(
            final MultipartFile passport,
            final MultipartFile selfiePhoto
    ) {
        if (passport == null || selfiePhoto == null) {
            String message = "Passport or selfie file missing!";
            log.error(message);
            throw new InvalidPassportException(message);
        }
        log.info("Comparing faces...");
        FaceApiResponse faceApiResponse =
                this.compareFaces(passport, selfiePhoto);

        if (faceApiResponse == null) {
            String message = "Face api response is NULL!";
            log.error(message);
            throw new InvalidPassportException(message);
        }

        log.info(String.format(
            "The probability that the faces are the same is %,.2f percent.",
            PERCENT_MULTIPLIER * faceApiResponse.getConfidence())
        );

        if (Boolean.FALSE.equals(faceApiResponse.getIsIdentical())) {
            String message =
                    "Passport photo is not matching with the selfie!";
            throw new InvalidPassportException(message);
        }
    }

    /**
     * Returns the headers for the API call.
     *
     * @return The headers for the API call.
     */
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
    @SuppressWarnings("checkstyle:LineLength")
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
            Type listType = new TypeToken<List<FaceDetectionResponse>>() { }
                    .getType();

            ArrayList<FaceDetectionResponse> faceDetectionResponses = gson
                    .fromJson(
                        detectionString,
                        listType
            );
            assert faceDetectionResponses != null;
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

    /**
     * Returns the face ID for the given byte array.
     *
     * @param fileBytes the file to be processed.
     * @return the face ID for the given file.
     */
    @SuppressWarnings("checkstyle:LineLength")
    public String getFaceIdForFile(final byte[] fileBytes) {
        String faceApiDetectionEndpoint = String.format(
                "%sface/v1.0/detect?returnFaceId=true&recognitionModel=recognition_04&faceIdTimeToLive=300",
                this.faceApiUrl
        );

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
        Type listType = new TypeToken<List<FaceDetectionResponse>>() { }
                .getType();

        ArrayList<FaceDetectionResponse> faceDetectionResponses = gson
                .fromJson(
                        detectionString,
                        listType
                );
        assert faceDetectionResponses != null;
        faceDetectionResponses.sort((face1, face2) -> {
            Integer area1 = face1.getFaceRectangle().getArea();
            Integer area2 = face2.getFaceRectangle().getArea();

            return area2.compareTo(area1);
        });

        return faceDetectionResponses.get(0).getFaceId();
    }

    /**
     * Compares the faces found on the passport and the
     * selfie, and then sends it back to the client.
     *
     * @param passport The user's passport.
     * @param selfiePhoto The selfie of the user.
     * @return The validity and the percentage of the matching.
     */
    private FaceApiResponse compareFaces(
            final MultipartFile passport,
            final MultipartFile selfiePhoto
    ) {
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

        HttpEntity<String> requestEntity = new HttpEntity<>(
                jsonString,
                headers
        );

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
     * selfie, and then sends it back to the client.
     *
     * @param passport The user's passport.
     * @param selfiePhoto The selfie of the user.
     * @return The validity and the percentage of the matching.
     */
    private FaceApiResponse compareFaces(
            final byte[] passport,
            final byte[] selfiePhoto
    ) {
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

        HttpEntity<String> requestEntity = new HttpEntity<>(
                jsonString,
                headers
        );

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
     * selfie, and then sends it back to the client.
     *
     * @param passport The student's passport.
     * @param selfiePhoto The selfie of the student.
     * @return The validity and the percentage of the matching.
     */
    @Override
    public FaceApiResponse getValidityOfFaces(
            final MultipartFile passport,
            final MultipartFile selfiePhoto
    ) {

        return this.compareFaces(passport, selfiePhoto);
    }

    /**
     * Compares the faces found on the passport and the
     * selfie, and then sends it back to the client.
     *
     * @param passport The student's passport.
     * @param selfiePhoto The selfie of the student.
     * @return The validity and the percentage of the matching.
     */
    public FaceApiResponse getValidityOfFaces(
            final byte[] passport,
            final byte[] selfiePhoto
    ) {

        return this.compareFaces(passport, selfiePhoto);
    }

    /**
     * Validates the faces for the given passport number.
     *
     * @param passportNumber The passport number.
     * @param studentId The id of the student.
     */
    @Override
    public boolean validateFacesForPassportNumber(
            final String passportNumber,
            final Long studentId
    ) {
        byte[] passportBytes = this.fileUploadService
                .getImageForType(studentId, StudentImageType.PASSPORT);
        byte[] selfieBytes = this.fileUploadService
                .getImageForType(studentId, StudentImageType.SELFIE);
        log.info(passportBytes.length + " " + selfieBytes.length);
        FaceApiResponse validityOfFaces = this
                .getValidityOfFaces(passportBytes, selfieBytes);

        FacialValidationData facialValidationData = FacialValidationData
                .builder()
                .passportNumber(passportNumber)
                .isValid(validityOfFaces.getIsIdentical())
                .percentage(validityOfFaces.getConfidence())
                .build();

        this.facialValidationService
                .saveFacialValidationData(facialValidationData);

        return validityOfFaces.getIsIdentical();
    }

    /**
     * Deletes the facial validation data for the given passport number.
     *
     * @param passportNumber The passport number.
     */
    @Override
    public void deleteFace(
            final String passportNumber
    ) {
        this.facialValidationService
                .deleteFacialValidationDataByPassportNumber(passportNumber);
    }
}
