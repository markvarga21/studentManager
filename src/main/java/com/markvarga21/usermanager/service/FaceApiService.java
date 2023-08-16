package com.markvarga21.usermanager.service;

import org.springframework.web.multipart.MultipartFile;

public interface FaceApiService {
    boolean compareFaces(MultipartFile idPhoto, MultipartFile selfiePhoto);
}
