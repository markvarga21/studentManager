package com.markvarga21.usermanager.dto;

import lombok.Data;


/**
 * A utility class for mapping a part of the
 * response from Azure's Face API to a POJO.
 */
@Data
public class FaceRectangle {
    /**
     * The top coordinate of the rectangle.
     */
    private Integer top;

    /**
     * The left coordinate of the rectangle.
     */
    private Integer left;

    /**
     * The width and height of the rectangle.
     */
    private Integer width;

    /**
     * The height of the rectangle.
     */
    private Integer height;

    /**
     * Calculates the area of the rectangle
     * representing the face.
     *
     * @return The area of the rectangle bounding the face.
     */
    public Integer getArea() {
        return this.width * this.height;
    }
}
