package com.markvarga21.studentmanager.service.form;

import com.azure.ai.formrecognizer.documentanalysis.models.DocumentField;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.dto.PassportValidationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * An interface containing methods for the Form Recognizer service.
 */
public interface FormRecognizerService {
    /**
     * Extracts and returns the data from the passport.
     *
     * @param passport The photo of the passport.
     * @return The extracted {@code StudentDto} object.
     */
    StudentDto extractDataFromPassport(MultipartFile passport);

    /**
     * Extracts all fields from the uploaded passport.
     *
     * @param passport The uploaded passport.
     * @return The extracted fields stored in a {@code Map}.
     */
    Map<String, DocumentField> getFieldsFromDocument(
            MultipartFile passport
    );

    /**
     * Validates the data entered by the user against the data
     * which can be found-, and has been extracted
     * from the passport.
     *
     * @param studentDto The student itself.
     * @param passport The photo of the passport.
     * @return A {@code PassportValidationResponse} object.
     */
    PassportValidationResponse validatePassport(
            String studentDto,
            MultipartFile passport
    );

    /**
     * Deletes a passport validation data
     * identified by the passport number.
     *
     * @param passportNumber the passport number.
     * @return A feedback message.
     */
    String deletePassportValidationByPassportNumber(
            String passportNumber
    );

    /**
     * Validates the passport manually.
     *
     * @param studentId The id of the student.
     * @return An informational message.
     */
    String validatePassportManually(Long studentId);

    /**
     * Checks if the user is valid or not.
     *
     * @param passportNumber The passport number.
     * @return {@code true} if the user is valid, {@code false} otherwise.
     */
    Boolean isUserValid(String passportNumber);
}
