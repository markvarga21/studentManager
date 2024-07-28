package com.markvarga21.studentmanager.dto;

import com.markvarga21.studentmanager.util.Generated;
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
@Generated
public class FaceApiResponse {
    /**
     * The validity of the face-matching.
     */
    private Boolean isIdentical;

    /**
     * The probability of how similar are the faces
     * on the passport and on the portrait
     * compared to each other.
     */
    private Double confidence;
}
