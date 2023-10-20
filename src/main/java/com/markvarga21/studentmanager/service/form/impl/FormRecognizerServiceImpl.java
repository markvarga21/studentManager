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
    private final StudentMapper userMapper;

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
     * @param student1 The first student.
     * @param student2 The second student.
     * @return {@code true} if the students are equal, {@code false} otherwise.
     */
    private boolean studentsAreEqual(
            final StudentDto student1,
            final StudentDto student2
    ) {
        StudentDto student1Clone = student1.clone();
        StudentDto student2Clone = student2.clone();
        student1Clone
                .setFirstName(student1.getFirstName().toLowerCase());
        student1Clone
                .setLastName(student1.getLastName().toLowerCase());
        student1Clone
                .setPlaceOfBirth(student1.getPlaceOfBirth().toLowerCase());
        student1Clone
                .setCountryOfCitizenship(student1.getCountryOfCitizenship().toLowerCase());

        student2Clone
                .setFirstName(student2.getFirstName().toLowerCase());
        student2Clone
                .setLastName(student2.getLastName().toLowerCase());
        student2Clone
                .setPlaceOfBirth(student2.getPlaceOfBirth().toLowerCase());
        student2Clone
                .setCountryOfCitizenship(student2.getCountryOfCitizenship().toLowerCase());

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
                .userMapper.mapJsonToDto(studentJson);

        if (this.isStudentPresentInValidationDatabase(studentDataFromUser)) {
            log.info("Student is present in the validation database.");
            return PassportValidationResponse.builder()
                    .isValid(true)
                    .build();
        } else {
            String firstName = studentDataFromUser
                    .getFirstName();
            String lastName = studentDataFromUser
                    .getLastName();
            studentDataFromUser.setFirstName(firstName);
            studentDataFromUser.setLastName(lastName);

            StudentDto userDataFromPassport = this
                    .extractDataFromPassport(passport);

            if (studentsAreEqual(userDataFromPassport, studentDataFromUser)) {
                PassportValidationData passportValidationData =
                        PassportValidationData.builder()
                                .firstName(userDataFromPassport.getFirstName())
                                .lastName(userDataFromPassport.getLastName())
                                .birthDate(userDataFromPassport.getBirthDate())
                                .placeOfBirth(userDataFromPassport.getPlaceOfBirth())
                                .passportNumber(userDataFromPassport.getPassportNumber())
                                .passportDateOfExpiry(userDataFromPassport.getPassportDateOfExpiry())
                                .passportDateOfIssue(userDataFromPassport.getPassportDateOfIssue())
                                .gender(userDataFromPassport.getGender())
                                .countryOfCitizenship(userDataFromPassport.getCountryOfCitizenship())
                                .timestamp(LocalDateTime.now())
                                .build();
                this.validationRepository.save(passportValidationData);

                return PassportValidationResponse.builder()
                        .isValid(true)
                        .build();
            }
            return PassportValidationResponse.builder()
                    .isValid(false)
                    .studentDto(userDataFromPassport)
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
