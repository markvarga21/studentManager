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
import com.markvarga21.studentmanager.exception.InvalidPassportException;
import com.markvarga21.studentmanager.repository.PassportValidationDataRepository;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.util.CountryNameFetcher;
import com.markvarga21.studentmanager.util.ImageCompressor;
import com.markvarga21.studentmanager.util.PassportDateFormatter;
import com.markvarga21.studentmanager.util.mapping.StudentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * A service which is used to verify the data entered by the user
 * against the data which can be found on the uploaded passport.
 * It uses Azure's Form Recognizer.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Validated
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
     * The passport date formatter.
     */
    private final PassportDateFormatter passportDateFormatter;

    /**
     * A util class for converting a country code to
     * the actual name of the country.
     */
    private final CountryNameFetcher countryNameFetcher;

    /**
     * A student mapper.
     */
    private final StudentMapper studentMapper;

    /**
     * A repository which is used to store the data extracted
     * from the passport while validation.
     */
    private final PassportValidationDataRepository validationRepository;

    /**
     * A util class for compressing images.
     */
    private final ImageCompressor imageCompressor;

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
        byte[] compressedPassport = this.imageCompressor
                .compressImage(passport);
        BinaryData binaryData = BinaryData.fromBytes(compressedPassport);
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
    }

    /**
     * Extracts and returns the data from the passport.
     *
     * @param passport The photo of the passport.
     * @return The extracted {@code StudentDto} object.
     */
    @Override
    public StudentDto extractDataFromPassport(final MultipartFile passport) {
        Map<String, DocumentField> passportFields = this
                .getFieldsFromDocument(passport);
        String firstName = this
                .getFieldValue(passportFields, "FirstName");
        String lastName = this
                .getFieldValue(passportFields, "LastName");
        String birthdateField = this
                .getFieldValue(passportFields, "DateOfBirth");
        LocalDate birthDate = this.passportDateFormatter.format(birthdateField);
        String placeOfBirth = this
                .getFieldValue(passportFields, "PlaceOfBirth");
        String countryCode = this
                .getFieldValue(passportFields, "CountryRegion");
        String countryOfCitizenship = this.countryNameFetcher
                .getCountryNameForCode(countryCode);
        Gender gender = this.getFieldValue(passportFields, "Sex").equals("M")
                ? Gender.MALE
                : Gender.FEMALE;
        String passportNumber = this
                .getFieldValue(passportFields, "DocumentNumber");
        String dateOfExpiryField = this
                .getFieldValue(passportFields, "DateOfExpiration");
        LocalDate dateOfExpiry = this.passportDateFormatter
                .format(dateOfExpiryField);
        String dateOfIssueField = this
                .getFieldValue(passportFields, "DateOfIssue");
        LocalDate dateOfIssue = this.passportDateFormatter
                .format(dateOfIssueField);

        return StudentDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate)
                .placeOfBirth(placeOfBirth)
                .countryOfCitizenship(countryOfCitizenship)
                .gender(gender)
                .passportNumber(passportNumber)
                .passportDateOfExpiry(dateOfExpiry)
                .passportDateOfIssue(dateOfIssue)
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
     * Compares the data entered by the user against the data.
     * It is used due to the inconsistencies (upper- or lower cases)
     * of the names.
     *
     * @param studentDataFromPassport The first student.
     * @param studentDataFromUser The second student.
     * @return {@code true} if the students are equal, {@code false} otherwise.
     */
    private boolean studentsAreEqual(
            final StudentDto studentDataFromPassport,
            final StudentDto studentDataFromUser
    ) {
        StudentDto student1Clone = studentDataFromPassport.clone();
        StudentDto student2Clone = studentDataFromUser.clone();
        student1Clone
                .setFirstName(studentDataFromPassport.getFirstName().toLowerCase());
        student1Clone
                .setLastName(studentDataFromPassport.getLastName().toLowerCase());
        student1Clone
                .setPlaceOfBirth(studentDataFromPassport.getPlaceOfBirth().toLowerCase());
        student1Clone
                .setCountryOfCitizenship(studentDataFromPassport.getCountryOfCitizenship().toLowerCase());

        student2Clone
                .setFirstName(studentDataFromUser.getFirstName().toLowerCase());
        student2Clone
                .setLastName(studentDataFromUser.getLastName().toLowerCase());
        student2Clone
                .setPlaceOfBirth(studentDataFromUser.getPlaceOfBirth().toLowerCase());
        student2Clone
                .setCountryOfCitizenship(studentDataFromUser.getCountryOfCitizenship().toLowerCase());
        
        student1Clone.setId(null);
        student2Clone.setId(null);

        if (studentDataFromPassport.getBirthDate() == null) {
            if (studentDataFromUser.getBirthDate() == null) {
                log.info("Both filled and extracted birth dates are null.");
                return false;
            }
            log.info("Extracted birth date is null, but filled birth date is not.");
            student2Clone.setBirthDate(null);
        }

        if (studentDataFromPassport.getPassportDateOfIssue() == null) {
            if (studentDataFromUser.getPassportDateOfIssue() == null) {
                log.info("Both filled and extracted passport date of issues are null.");
                return false;
            }
            log.info("Extracted passport date of issue is null, but filled passport date of issue is not.");
            student2Clone.setPassportDateOfIssue(null);
        }

        if (studentDataFromPassport.getPassportDateOfExpiry() == null) {
            if (studentDataFromUser.getPassportDateOfExpiry() == null) {
                log.info("Both filled and extracted passport date of expiry are null.");
                return false;
            }
            log.info("Extracted passport date of expiry is null, but filled passport date of expiry is not.");
            student2Clone.setPassportDateOfExpiry(null);
        }
        log.info("Student 1 clone: {}", student1Clone);
        log.info("Student 2 clone: {}", student2Clone);
        return student1Clone.equals(student2Clone);
    }

    /**
     * Validates the data entered by the user against the data
     * which can be found on the passport.
     *
     * @param passport The photo of the passport.
     * @param studentJson The student itself in a JSON string.
     * @return A {@code PassportValidationResponse} object.
     */
    @Override
    public PassportValidationResponse validatePassport(
            final MultipartFile passport,
            final String studentJson
    ) {
        StudentDto studentDataFromUser = this
                .studentMapper.mapJsonToDto(studentJson);

        if (this.isStudentPresentInValidationDatabase(studentDataFromUser)) {
            log.info("Student is present in the validation database.");
            return PassportValidationResponse.builder()
                    .isValid(true)
                    .build();
        } else {
            log.info("Student is not present in the validation database. Extracting data...");
            String firstName = studentDataFromUser
                    .getFirstName();
            String lastName = studentDataFromUser
                    .getLastName();
            studentDataFromUser.setFirstName(firstName);
            studentDataFromUser.setLastName(lastName);

            StudentDto studentDataFromPassport = this
                    .extractDataFromPassport(passport);

            log.info("Student data from passport: {}", studentDataFromPassport);
            log.info("Student data from user: {}", studentDataFromUser);

            if (studentsAreEqual(studentDataFromPassport, studentDataFromUser)) {
                PassportValidationData passportValidationData =
                        PassportValidationData.builder()
                                .firstName(studentDataFromPassport.getFirstName())
                                .lastName(studentDataFromPassport.getLastName())
                                .birthDate(studentDataFromPassport.getBirthDate())
                                .placeOfBirth(studentDataFromPassport.getPlaceOfBirth())
                                .passportNumber(studentDataFromPassport.getPassportNumber())
                                .passportDateOfExpiry(studentDataFromPassport.getPassportDateOfExpiry())
                                .passportDateOfIssue(studentDataFromPassport.getPassportDateOfIssue())
                                .gender(studentDataFromPassport.getGender())
                                .countryOfCitizenship(studentDataFromPassport.getCountryOfCitizenship())
                                .timestamp(LocalDateTime.now())
                                .build();
                if (studentDataFromPassport.getBirthDate() == null) {
                    passportValidationData
                            .setBirthDate(studentDataFromUser.getBirthDate());
                }

                if (studentDataFromPassport.getPassportDateOfExpiry() == null) {
                    passportValidationData
                            .setPassportDateOfExpiry(studentDataFromUser.getPassportDateOfExpiry());
                }

                if (studentDataFromPassport.getPassportDateOfIssue() == null) {
                    passportValidationData
                            .setPassportDateOfIssue(studentDataFromUser.getPassportDateOfIssue());
                }

                this.validationRepository.save(passportValidationData);

                return PassportValidationResponse.builder()
                        .isValid(true)
                        .build();
            }
            return PassportValidationResponse.builder()
                    .isValid(false)
                    .studentDto(studentDataFromPassport)
                    .build();
        }
    }

    /**
     * Checks if the student is present in the validation database.
     *
     * @param studentDto The student.
     * @return An {@code Optional} object.
     */
    @Override
    public boolean isStudentPresentInValidationDatabase(
            final StudentDto studentDto
    ) {
        List<PassportValidationData> passportValidations = this
                .validationRepository.findAll();
        log.info("Student dto: {}", studentDto);
        log.info("Passport validations: {}", passportValidations);
        return !passportValidations.stream()
                .filter(passportValidationData ->
                    passportValidationData.getFirstName()
                            .equalsIgnoreCase(studentDto.getFirstName())
                            && passportValidationData.getLastName()
                            .equalsIgnoreCase(studentDto.getLastName())
                            && passportValidationData.getBirthDate()
                            .equals(studentDto.getBirthDate())
                            && passportValidationData.getPlaceOfBirth()
                            .equalsIgnoreCase(studentDto.getPlaceOfBirth())
                            && passportValidationData.getPassportNumber()
                            .equals(studentDto.getPassportNumber())
                            && passportValidationData.getPassportDateOfExpiry()
                            .equals(studentDto.getPassportDateOfExpiry())
                            && passportValidationData.getPassportDateOfIssue()
                            .equals(studentDto.getPassportDateOfIssue())
                            && passportValidationData.getGender()
                            .equals(studentDto.getGender())
                            && passportValidationData.getCountryOfCitizenship()
                            .equalsIgnoreCase(studentDto.getCountryOfCitizenship())
                )
                .toList()
                .isEmpty();
    }

    /**
     * Deletes the passport validation data by the passport number.
     *
     * @param passportNumber the passport number.
     */
    @Override
    public void deletePassportValidationByPassportNumber(
            final String passportNumber
    ) {
        this.validationRepository.deletePassportValidationDataByPassportNumber(
                passportNumber
        );
    }
}
