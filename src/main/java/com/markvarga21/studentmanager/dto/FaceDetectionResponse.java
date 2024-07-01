package com.markvarga21.studentmanager.dto;

import com.markvarga21.studentmanager.util.Generated;
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
@Generated
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
