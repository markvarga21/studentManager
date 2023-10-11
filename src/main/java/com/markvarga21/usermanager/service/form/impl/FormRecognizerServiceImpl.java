package com.markvarga21.usermanager.service.form.impl;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentField;
import com.azure.ai.formrecognizer.documentanalysis.models.OperationResult;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;
import com.markvarga21.usermanager.dto.StudentDto;
import com.markvarga21.usermanager.dto.PassportValidationResponse;
import com.markvarga21.usermanager.entity.Gender;
import com.markvarga21.usermanager.entity.PassportValidationData;
import com.markvarga21.usermanager.exception.InvalidPassportException;
import com.markvarga21.usermanager.repository.PassportValidationDataRepository;
import com.markvarga21.usermanager.service.form.FormRecognizerService;
import com.markvarga21.usermanager.util.CountryNameFetcher;
import com.markvarga21.usermanager.util.PassportDateFormatter;
import com.markvarga21.usermanager.util.mapping.AppUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * A service which is used to verify the data entered by the user
 * against the data which can be found on either the uploaded
 * ID document or passport. It uses Azure's Form Recognizer.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Validated
public class FormRecognizerServiceImpl implements FormRecognizerService {
    /**
     * The default address if the address field is empty.
     */
    public static final String EMPTY_ADDRESS = "Not known";
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
     * A user mapper.
     */
    private final AppUserMapper userMapper;

    /**
     * A repository which is used to store the data extracted
     * from the passport while validation.
     */
    private final PassportValidationDataRepository validationRepository;

    /**
     * Extracts all the fields from the uploaded ID document.
     *
     * @param idDocument the uploaded ID document or passport.
     * @return the extracted fields stored in a {@code Map}.
     */
    @Override
    public Map<String, DocumentField> getFieldsFromDocument(
            final MultipartFile idDocument
    ) {
        try {
            BinaryData binaryData = BinaryData.fromBytes(idDocument.getBytes());
            String modelId = "prebuilt-idDocument";
            SyncPoller<OperationResult, AnalyzeResult> analyzeDocumentPoller =
                    this.documentAnalysisClient.beginAnalyzeDocument(
                            modelId, binaryData
                    );

            AnalyzeResult analyzeResult =
                    analyzeDocumentPoller.getFinalResult();
            var documentResult = analyzeResult.getDocuments().get(0);
            return documentResult.getFields();
        } catch (IOException e) {
            String message = "ID document not found!";
            log.error(message);
            throw new InvalidPassportException(message);
        }
    }

    /**
     * Extracts and returns the data from the passport.
     *
     * @param passport the photo of the passport.
     * @return the extracted {@code AppUserDto} object.
     */
    @Override
    public StudentDto extractDataFromPassport(final MultipartFile passport) {
        Map<String, DocumentField> passportFields = this
                .getFieldsFromDocument(passport);
        String firstName = passportFields.get("FirstName").getContent();
        String lastName = passportFields.get("LastName").getContent();
        String birthdateField = passportFields
                .get("DateOfBirth")
                .getContent();
        LocalDate birthDate = this.passportDateFormatter.format(birthdateField);
        String address = passportFields.get("Address") == null
                ? EMPTY_ADDRESS
                : passportFields.get("Address").getContent();
        String countryCode = passportFields.get("CountryRegion")
                .getContent();
        String countryOfCitizenship = this.countryNameFetcher
                .getCountryNameForCode(countryCode);
        Gender gender = passportFields.get("Sex").getContent().equals("M")
                ? Gender.MALE
                : Gender.FEMALE;
        String passportNumber = passportFields.get("DocumentNumber")
                .getContent();
        String dateOfExpiryField = passportFields.get("DateOfExpiration")
                .getContent();
        LocalDate dateOfExpiry = this.passportDateFormatter
                .format(dateOfExpiryField);
        String dateOfIssueField = passportFields.get("DateOfIssue")
                .getContent();
        LocalDate dateOfIssue = this.passportDateFormatter
                .format(dateOfIssueField);

        return StudentDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate)
                .placeOfBirth(address)
                .countryOfCitizenship(countryOfCitizenship)
                .gender(gender)
                .passportNumber(passportNumber)
                .passportDateOfExpiry(dateOfExpiry)
                .passportDateOfIssue(dateOfIssue)
                .build();
    }

    /**
     * Validates the data entered by the user against the data
     * which can be found on the passport.
     *
     * @param passport the photo of the passport.
     * @param appUserJson the user itself in a JSON string.
     * @return a {@code PassportValidationResponse} object.
     */
    @Override
    public PassportValidationResponse validatePassport(
            final MultipartFile passport,
            final String appUserJson
    ) {
        StudentDto userDataFromUser = this
                .userMapper.mapJsonToDto(appUserJson);
        if (this.isUserPresentInValidationDatabase(userDataFromUser)) {
            log.info("User is present in the validation database.");
            return PassportValidationResponse.builder()
                    .isValid(true)
                    .build();
        } else {
            StudentDto userDataFromPassport = this
                    .extractDataFromPassport(passport);
            if (userDataFromPassport.equals(userDataFromUser)) {
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
     * Checks if the user is present in the validation database.
     *
     * @param studentDto the user.
     * @return an {@code Optional} object.
     */
    @Override
    public boolean isUserPresentInValidationDatabase(
            final StudentDto studentDto
    ) {
        List<PassportValidationData> passportValidations = this
                .validationRepository.findAll();
        return !passportValidations.stream()
                .filter(passportValidationData ->
                    passportValidationData.getFirstName()
                            .equals(studentDto.getFirstName())
                            && passportValidationData.getLastName()
                            .equals(studentDto.getLastName())
                            && passportValidationData.getBirthDate()
                            .equals(studentDto.getBirthDate())
                            && passportValidationData.getPlaceOfBirth()
                            .equals(studentDto.getPlaceOfBirth())
                            && passportValidationData.getPassportNumber()
                            .equals(studentDto.getPassportNumber())
                            && passportValidationData.getPassportDateOfExpiry()
                            .equals(studentDto.getPassportDateOfExpiry())
                            && passportValidationData.getPassportDateOfIssue()
                            .equals(studentDto.getPassportDateOfIssue())
                            && passportValidationData.getGender()
                            .equals(studentDto.getGender())
                            && passportValidationData.getCountryOfCitizenship()
                            .equals(studentDto.getCountryOfCitizenship())
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
