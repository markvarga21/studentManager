package com.markvarga21.usermanager.service.azure;

import com.markvarga21.usermanager.dto.AppUserDto;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

/**
 * An interface containing methods for the Form Recognizer service.
 */
public interface FormRecognizerService {
    /**
     * Validates a user based on the forms data and the data found
     * on the ID card or passport.
     *
     * @param appUserDto the user which is used for validation.
     * @param idDocument the ID document or passport.
     * @param identification the identification type. Can be either 'idDocument' or 'passport'.
     */
    void validateUser(@Valid AppUserDto appUserDto, MultipartFile idDocument, String identification);
}
