package com.markvarga21.usermanager.service.form;

import com.azure.ai.formrecognizer.documentanalysis.models.DocumentField;
import com.markvarga21.usermanager.dto.StudentDto;
import com.markvarga21.usermanager.dto.PassportValidationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * An interface containing methods for the Form Recognizer service.
 */
public interface FormRecognizerService {
    /**
     * Extracts and returns the data from the passport.
     *
     * @param passport the photo of the passport.
     * @return the extracted {@code AppUserDto} object.
     */
    StudentDto extractDataFromPassport(MultipartFile passport);

    /**
     * Extracts all the fields from the uploaded ID document.
     *
     * @param idDocument the uploaded ID document or passport.
     * @return the extracted fields stored in a {@code Map}.
     */
    Map<String, DocumentField> getFieldsFromDocument(
            MultipartFile idDocument
    );

    /**
     * Validates the data entered by the user against the data
     * which can be found on the passport.
     *
     * @param passport the photo of the passport.
     * @param appUserJson the user itself in a JSON string.
     * @return a {@code PassportValidationResponse} object.
     */
    PassportValidationResponse validatePassport(
            MultipartFile passport,
            String appUserJson
    );

    /**
     * Checks if the user is present in the validation database.
     *
     * @param studentDto the user to be checked.
     * @return {@code true} if the user is present in the database,
     */
    boolean isUserPresentInValidationDatabase(
            StudentDto studentDto
    );

    /**
     * Deletes the passport validation data by the passport number.
     *
     * @param passportNumber the passport number.
     */
    void deletePassportValidationByPassportNumber(
            String passportNumber
    );
}
