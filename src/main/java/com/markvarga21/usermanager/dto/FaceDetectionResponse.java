package com.markvarga21.usermanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A utility class for mapping the main
 * response from Azure's Face API to a POJO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaceDetectionResponse {
    /**
     * The unique identifier of the face.
     */
    private String faceId;

    /**
     * The rectangle which bounds the face.
     */
    private FaceRectangle faceRectangle;
}
