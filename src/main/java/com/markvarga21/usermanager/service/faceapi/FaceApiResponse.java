package com.markvarga21.usermanager.service.faceapi;

import lombok.Data;
import lombok.ToString;

/**
 * A util class for correctly mapping
 * the response from the Face API.
 */
@Data
@ToString
public class FaceApiResponse {
    /**
     * The validity of the face-matching.
     */
    private Boolean isValid;

    /**
     * The probability of how similar ar the photos
     * compared to each other.
     */
    private Double proba;
}
