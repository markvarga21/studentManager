package com.markvarga21.usermanager.dto;

import lombok.Data;

@Data
public class FaceRectangle {
    private Integer top;
    private Integer left;
    private Integer width;
    private Integer height;

    public Integer getArea() {
        return this.width * this.height;
    }
}
