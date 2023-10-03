package com.markvarga21.usermanager.service.azure.impl;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentField;
import com.azure.ai.formrecognizer.documentanalysis.models.OperationResult;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;
import com.markvarga21.usermanager.dto.AddressDto;
import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.entity.Gender;
import com.markvarga21.usermanager.exception.InvalidIdDocumentException;
import com.markvarga21.usermanager.service.azure.FormRecognizerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
     * A client which is used to analyze documents.
     */
    private final DocumentAnalysisClient documentAnalysisClient;

    /**
     * A method which checks whether the data entered by the user is the same
     * as in the uploaded ID document or passport.
     *
     * @param appUserDto the user which has to be validated.
     * @param idDocument the ID document or passport of the user.
     * @param identification the document type ('passport' or 'idDocument').
     */
    @Override
    public void validateUser(
            @Valid final AppUserDto appUserDto,
            final MultipartFile idDocument,
            final String identification
    ) {
        if (!isValidIdContent(appUserDto, idDocument, identification)) {
            log.error("Invalid content!");
            throw new InvalidIdDocumentException("Invalid content!");
        }
        log.info("Valid content!");
    }

    /**
     * Extracts all the fields from the uploaded ID document.
     *
     * @param idDocument the uploaded ID document or passport.
     * @return the extracted fields stored in a {@code Map}.
     */
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
            throw new InvalidIdDocumentException(message);
        }
    }

    /**
     * Checks if the content of the user's inputted data
     * matches the data on the ID document.
     *
     * @param appUserDto the user which has to be validated.
     * @param idDocument the identification document.
     * @param identification the ID type ('idDocument' or 'passport').
     * @return {@code true} if all the data from the form matches
     * the data extracted from the ID document.
     */
    public boolean isValidIdContent(
            @Valid final AppUserDto appUserDto,
            final MultipartFile idDocument,
            final String identification
    ) {
        Map<String, DocumentField> fields =
                getKeyValuePairsFromPassport(idDocument);
        String firstName = fields.get("FirstName").getContent();
        String lastName = fields.get("LastName").getContent();
        String birthDate = fields
                .get("DateOfBirth")
                .getContent()
                .replace(".", " ");

        log.info("ID lastName = " + lastName + ", firstName = " + firstName);

        String formFirstName = appUserDto.getFirstName();
        String formLastName = appUserDto.getLastName();

        String dateFormat = identification
                .equalsIgnoreCase("passport")
                ? "dd MMM/MMM yyyy" : "dd MM yyyy";

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern(dateFormat);
        String formBirthDate = appUserDto
                .getBirthDate()
                .format(dateTimeFormatter)
                .replace(".", "");

        log.info(String.format("FORM lastName = %s, firstName = %s",
                formLastName,
                formFirstName)
        );
        log.info(String.format(
                "Form birthdate = %s, id birthdate = %s",
                formBirthDate,
                birthDate)
        );

        if (!formFirstName.equalsIgnoreCase(firstName)) {
            String message = String.format(
                "Form's first name '%s' not matching with ID's first name '%s'",
                formFirstName,
                firstName
            );
            log.error(message);
            throw new InvalidIdDocumentException(message);
        }

        if (!formLastName.equalsIgnoreCase(lastName)) {
            String message = String.format(
                "Form's last name '%s' not matching with ID's last name '%s'",
                formLastName,
                lastName
            );
            log.error(message);
            throw new InvalidIdDocumentException(message);
        }

        if (!formBirthDate.equalsIgnoreCase(birthDate)) {
            String message = String.format(
                "Form's birth date '%s' not matching with ID's birth date '%s'",
                formBirthDate,
                birthDate
            );
            log.error(message);
            throw new InvalidIdDocumentException(message);
        }

        if (identification.equalsIgnoreCase("passport")) {
            DocumentField nationality = fields.get("Nationality");
            if (nationality == null) {
                String message = "Nationality not present or not readable!";
                throw new InvalidIdDocumentException(message);
            }
            String normalizedNationality = nationality
                    .getContent()
                    .toLowerCase();
            String normalizedAppUserNationality = appUserDto
                    .getCountryOfCitizenship()
                    .toLowerCase();
            if (!normalizedNationality.contains(normalizedAppUserNationality)) {
                String message = String.format(
                    "Invalid Nationality: '%s' not equals with '%s'",
                    nationality,
                    appUserDto.getCountryOfCitizenship()
                );
                log.error(message);
                throw new InvalidIdDocumentException(message);
            }
        }

        return true;
    }

    /**
     * Extracts all the available key-value pairs from
     * the uploaded passport.
     *
     * @param passport the user's passport.
     * @return the extracted kay-value pairs.
     */
    @Override
    public Map<String, DocumentField> getKeyValuePairsFromPassport(
            final MultipartFile passport
    ) {
        // TODO remove this
        return null;
//        return getFieldsFromDocument(passport);
    }

    /**
     * Converts a map, which contains all the extracted data
     * from the passport, to an {@code AppUserDto}.
     *
     * @param passportData a map containing all the passport's data.
     * @return the formed {@code AppUserDto}.
     */
    private AppUserDto convertDocumentMapToAppUserDto(
            final Map<String, DocumentField> passportData
    ) {
        // TODO remove this static inlined content
        AddressDto addressDto = AddressDto.builder()
                .country("Hungary")
                .city("Debrecen")
                .street("Piac")
                .number(5)
                .build();
        log.info("Building user from passport data.");
        return AppUserDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .birthDate(LocalDate.of(1970, 1, 1))
                .gender(Gender.MALE)
                .phoneNumber("123456789")
                .placeOfBirth(addressDto)
                .passportNumber("123456789")
                .countryOfCitizenship("Hungary")
                .passportDateOfExpiry(LocalDate.of(2030, 1, 1))
                .passportDateOfIssue(LocalDate.of(1970, 1, 1))
                .build();
    }

    /**
     * Extracts and returns the data from the passport.
     *
     * @param passport the photo of the passport.
     * @return the extracted {@code AppUserDto} object.
     */
    @Override
    public AppUserDto extractDataFromPassport(final MultipartFile passport) {
        Map<String, DocumentField> passportData = this
                .getKeyValuePairsFromPassport(passport);
        return this.convertDocumentMapToAppUserDto(passportData);
    }
}
