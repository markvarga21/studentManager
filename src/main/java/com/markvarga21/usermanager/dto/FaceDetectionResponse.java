package com.markvarga21.usermanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaceDetectionResponse {
    private String faceId;
    private FaceRectangle faceRectangle;
}
