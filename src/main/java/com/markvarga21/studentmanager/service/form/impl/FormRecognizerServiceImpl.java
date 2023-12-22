package com.markvarga21.studentmanager.service.form.impl;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentField;
import com.azure.ai.formrecognizer.documentanalysis.models.OperationResult;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.dto.PassportValidationResponse;
import com.markvarga21.studentmanager.entity.Gender;
import com.markvarga21.studentmanager.entity.PassportValidationData;
import com.markvarga21.studentmanager.exception.*;
import com.markvarga21.studentmanager.repository.PassportValidationDataRepository;
import com.markvarga21.studentmanager.service.StudentService;
import com.markvarga21.studentmanager.service.faceapi.FaceApiService;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.service.validation.passport.PassportValidationService;
import com.markvarga21.studentmanager.util.CountryNameFetcher;
import com.markvarga21.studentmanager.util.DateDeserializer;
import com.markvarga21.studentmanager.util.mapping.StudentMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

/**
 * A service which is used to verify the data entered by the user
 * against the data which can be found on the uploaded passport.
 * It uses Azure's Form Recognizer.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FormRecognizerServiceImpl implements FormRecognizerService {
    /**
     * The default value if the field is empty.
     */
    public static final String EMPTY_FIELD_VALUE = "";

    /**
     * A client which is used to analyze documents.
     */
    private final DocumentAnalysisClient documentAnalysisClient;

    /**
     * A service which is used to access passport
     * validation data.
     */
    private final PassportValidationService passportValidationService;

    /**
     * A util class for converting a country code to
     * the actual name of the country.
     */
    private final CountryNameFetcher countryNameFetcher;

    /**
     * A repository which is used to store the data extracted
     * from the passport while validation.
     */
    private final PassportValidationDataRepository validationRepository;

    /**
     * A service which is used to manipulate the student data.
     */
    private final StudentService studentService;

    /**
     * A service which is used to manipulate the face data.
     */
    private final FaceApiService faceApiService;

    /**
     * A mapper which is used to map the student data
     * from JSON string to POJO.
     */
    private final StudentMapper studentMapper;

    /**
     * Extracts all the fields from the uploaded passport.
     *
     * @param passport The uploaded passport.
     * @return The extracted fields stored in a {@code Map}.
     */
    @Override
    public Map<String, DocumentField> getFieldsFromDocument(
            final MultipartFile passport
    ) {
        try {
            BinaryData binaryData = BinaryData.fromBytes(passport.getBytes());
            String modelId = "prebuilt-idDocument";
            SyncPoller<OperationResult, AnalyzeResult> analyzeDocumentPoller =
                    this.documentAnalysisClient.beginAnalyzeDocument(
                            modelId, binaryData
                    );

            AnalyzeResult analyzeResult =
                    analyzeDocumentPoller.getFinalResult();
            var documentResult = analyzeResult.getDocuments();
            if (documentResult == null) {
                throw new InvalidPassportException(
                        "Invalid passport!"
                );
            }
            return documentResult.get(0).getFields();
        } catch (IOException e) {
            throw new InvalidDocumentException(
                    "Invalid document!"
            );
        }
    }

    /**
     * Extracts and returns the data from the passport.
     *
     * @param passport The photo of the passport.
     * @return The extracted {@code StudentDto} object.
     */
    @Override
    @Transactional
    public StudentDto extractDataFromPassport(
            final MultipartFile passport
    ) {
        log.info("Extracting data from passport!");
        Map<String, DocumentField> passportFields = this
                .getFieldsFromDocument(passport);
        String firstName = this
                .getFieldValue(passportFields, "FirstName");
        String lastName = this
                .getFieldValue(passportFields, "LastName");
        String birthdateField = this
                .getFieldValue(passportFields, "DateOfBirth");
        LocalDate birthDate = DateDeserializer.mapDateStringToLocalDate(birthdateField);
        String placeOfBirth = this
                .getFieldValue(passportFields, "PlaceOfBirth");
        String countryCode = this
                .getFieldValue(passportFields, "CountryRegion");
        String countryOfCitizenship = this
                .countryNameFetcher.getCountryNameForCode(countryCode);
        Gender gender = this.getFieldValue(passportFields, "Sex").equals("M")
                ? Gender.MALE
                : Gender.FEMALE;
        String passportNumber = this
                .getFieldValue(passportFields, "DocumentNumber");
        String dateOfExpiryField = this
                .getFieldValue(passportFields, "DateOfExpiration");
        LocalDate dateOfExpiry = DateDeserializer
                .mapDateStringToLocalDate(dateOfExpiryField);
        String dateOfIssueField = this
                .getFieldValue(passportFields, "DateOfIssue");
        LocalDate dateOfIssue = DateDeserializer
                .mapDateStringToLocalDate(dateOfIssueField);

        return StudentDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(DateDeserializer.mapLocalDateToDateString(birthDate))
                .placeOfBirth(placeOfBirth)
                .countryOfCitizenship(countryOfCitizenship)
                .gender(gender)
                .passportNumber(passportNumber)
                .passportDateOfExpiry(DateDeserializer.mapLocalDateToDateString(dateOfExpiry))
                .passportDateOfIssue(DateDeserializer.mapLocalDateToDateString(dateOfIssue))
                .build();
    }

    /**
     * Extracts a field from a document map.
     *
     * @param fields The fields {@code Map}.
     * @param fieldName The name of the field.
     * @return The extracted field in a {@code String}.
     */
    private String getFieldValue(
            final Map<String, DocumentField> fields,
            final String fieldName
    ) {
        DocumentField value = fields
                .get(fieldName);
        if (value == null) {
            return EMPTY_FIELD_VALUE;
        }
        return value.getContent();
    }

    /**
     * Validates the data entered by the user against the data
     * which can be found on the passport.
     *
     * @param studentJson The student itself in a JSON string.
     * @param passport The passport file.
     * @return A {@code PassportValidationResponse} object.
     */
    @Override
    @Transactional
    public PassportValidationResponse validatePassport(
            final String studentJson,
            final MultipartFile passport
    ) {
        StudentDto studentDataFromUser = this.studentMapper
                .mapJsonToDto(studentJson);
        boolean facesAreMatching = this.faceApiService.validateFacesForPassportNumber(
                studentDataFromUser.getPassportNumber(),
                studentDataFromUser.getId()
        );

        if (!facesAreMatching) {
            return PassportValidationResponse.builder()
                    .isValid(false)
                    .studentDto(null)
                    .build();
        }

        log.info("Validating passport for user: {}", studentDataFromUser);
        String passportNumber = studentDataFromUser.getPassportNumber();
        Optional<PassportValidationData> passportValidationDataOptional
                = this.passportValidationService
                .getPassportValidationDataByPassportNumber(
                        passportNumber
                );
        if (passportValidationDataOptional.isEmpty() && passport == null) {
            log.error("Passport validation data not found for user: {}", passportNumber);
            return PassportValidationResponse.builder()
                    .isValid(false)
                    .studentDto(studentDataFromUser)
                    .build();
        }

        Optional<PassportValidationData> passportValidationData
                = this.passportValidationService.getPassportValidationDataByPassportNumber(
                        studentDataFromUser.getPassportNumber()
        );
        if (passportValidationData.isEmpty()) {
            StudentDto studentFromPassport = this.extractDataFromPassport(passport);
            Optional<PassportValidationData> data =
                    this.passportValidationService.getPassportValidationDataByPassportNumber(
                            studentFromPassport.getPassportNumber()
                    );
            if (data.isEmpty()) {
                String message = String.format("Passport validation data not found for passport number: '%s'", passportNumber);
                log.error(message);
                throw new InvalidPassportException(message);
            }

            return PassportValidationResponse.builder()
                    .isValid(false)
                    .studentDto(studentFromPassport)
                    .build();
        }

        log.info("Passport validation data found for user in the database: {}", passportNumber);
        StudentDto studentDataFromPassport =
            PassportValidationData.getStudentDtoFromValidationData(
                    passportValidationDataOptional.get()
            );
        if (studentDataFromUser.equals(studentDataFromPassport)) {
            log.info("Passport validation data valid!");
            return PassportValidationResponse.builder()
                    .isValid(true)
                    .studentDto(null)
                    .build();
        }
        log.error("Passport validation not valid for user: {}", studentDataFromUser);
        return PassportValidationResponse.builder()
                .isValid(false)
                .studentDto(studentDataFromPassport)
                .build();
    }

    /**
     * Deletes the passport validation data by the passport number.
     *
     * @param passportNumber the passport number.
     */
    @Override
    @Transactional
    public String deletePassportValidationByPassportNumber(
            final String passportNumber
    ) {
        Optional<PassportValidationData> passportValidationData =
                this.passportValidationService
                        .getPassportValidationDataByPassportNumber(passportNumber);
        if (passportValidationData.isPresent()) {
            this.validationRepository
                    .deletePassportValidationDataByPassportNumber(
                    passportNumber
            );
            return String.format(
                    "Passport validation data deleted for passport number: %s",
                    passportNumber
            );
        } else {
            String message = String.format("Passport validation data not found for passport number: %s", passportNumber);
            throw new PassportNotFoundException(message);
        }
    }

    /**
     * Validates the passport manually (usually by an admin).
     *
     * @param studentId The id of the student.
     */
    @Override
    public String validatePassportManually(final Long studentId) {
        return this.studentService.setValidity(studentId, true);
    }

    /**
     * Checks if the user is valid.
     *
     * @param passportNumber The passport number.
     * @return {@code true} if the user is valid, {@code false} otherwise.
     */
    @Override
    public Boolean isUserValid(final String passportNumber) {
        Optional<StudentDto> studentDtoOptional = this.studentService
                .getAllStudents()
                .stream()
                .filter(
                        studentDto -> studentDto.getPassportNumber().equals(passportNumber)
                ).findFirst();
        if (studentDtoOptional.isEmpty()) {
            String message = String.format("Student with passport number '%s' not found!", passportNumber);
            throw new StudentNotFoundException(message, OperationType.READ);
        }
        return studentDtoOptional.get().isValid();

    }
}
