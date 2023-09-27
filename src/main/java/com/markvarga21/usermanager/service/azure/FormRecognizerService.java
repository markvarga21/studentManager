package com.markvarga21.usermanager.service.azure;

import com.azure.ai.formrecognizer.documentanalysis.models.DocumentField;
import com.markvarga21.usermanager.dto.AppUserDto;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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
     * @param identification the ID type ('idDocument' or 'passport').
     */
    void validateUser(
            @Valid AppUserDto appUserDto,
            MultipartFile idDocument,
            String identification
    );

    /**
     * Extracts all the available key-value pairs from
     * the uploaded passport.
     *
     * @param passport the user's passport.
     * @return the extracted kay-value pairs.
     */
    Map<String, DocumentField> getKeyValuePairsFromPassport(
            MultipartFile passport
    );
}
