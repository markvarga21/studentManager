package com.markvarga21.usermanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A utility class for correctly mapping
 * the response from the Face API to POJO.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FaceApiResponse {
    /**
     * The validity of the face-matching.
     */
    private Boolean isIdentical;

    /**
     * The probability of how similar are the photos
     * compared to each other.
     */
    private Double confidence;
}
