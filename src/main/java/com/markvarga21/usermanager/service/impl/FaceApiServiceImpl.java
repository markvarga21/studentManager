package com.markvarga21.usermanager.service.impl;

import com.markvarga21.usermanager.service.FaceApiService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FaceApiServiceImpl implements FaceApiService {
    @Override
    public boolean compareFaces(MultipartFile idPhoto, MultipartFile selfiePhoto) {

        return false;
    }
}
