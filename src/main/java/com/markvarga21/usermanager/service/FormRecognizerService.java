package com.markvarga21.usermanager.service;

import com.markvarga21.usermanager.dto.AppUserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FormRecognizerService {
    boolean validateUser(AppUserDto appUserDto, MultipartFile idDocument);
}
