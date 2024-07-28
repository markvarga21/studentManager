package com.markvarga21.studentmanager.dto;

import com.markvarga21.studentmanager.util.Generated;
import lombok.Data;


/**
 * A utility class for mapping a part of the
 * response from Azure's Face API to a POJO.
 */
@Data
@Generated
public class FaceRectangle {
    /**
     * The top coordinate of the bounding rectangle.
     */
    private Integer top;

    /**
     * The left coordinate of the bounding rectangle.
     */
    private Integer left;

    /**
     * The width and height of the bounding rectangle.
     */
    private Integer width;

    /**
     * The height of the bounding rectangle.
     */
    private Integer height;

    /**
     * Calculates the area of the bounding rectangle
     * representing the face.
     *
     * @return The area of the rectangle bounding the face.
     */
    public Integer getArea() {
        return this.width * this.height;
    }
}
