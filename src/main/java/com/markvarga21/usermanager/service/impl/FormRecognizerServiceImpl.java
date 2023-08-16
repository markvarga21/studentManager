package com.markvarga21.usermanager.service.impl;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentField;
import com.azure.ai.formrecognizer.documentanalysis.models.OperationResult;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;
import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.exception.InvalidIdDocumentException;
import com.markvarga21.usermanager.service.FormRecognizerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.Document;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class FormRecognizerServiceImpl implements FormRecognizerService {
    private final DocumentAnalysisClient documentAnalysisClient;

    @Override
    public boolean validateUser(AppUserDto appUserDto, MultipartFile idDocument, String identification) {
        if (!isValidIdContent(appUserDto, idDocument, identification)) {
            log.error("Invalid content!");
            return false;
        }
        log.info("Valid content!");
        return true;
    }

    private Map<String, DocumentField> getFieldsFromDocument(MultipartFile idDocument) {
        try {
            BinaryData binaryData = BinaryData.fromBytes(idDocument.getBytes());
            String modelId = "prebuilt-idDocument";
            SyncPoller<OperationResult, AnalyzeResult> analyzeDocumentPoller =
                    this.documentAnalysisClient.beginAnalyzeDocument(modelId, binaryData);

            AnalyzeResult analyzeResult = analyzeDocumentPoller.getFinalResult();
            var documentResult = analyzeResult.getDocuments().get(0);
            return documentResult.getFields();
        } catch (IOException e) {
            String message = "ID document not found!";
            log.error(message);
            throw new InvalidIdDocumentException(message);
        }
    }

    private boolean isValidIdContent(AppUserDto appUserDto, MultipartFile idDocument, String identification) {
        var fields = getFieldsFromDocument(idDocument);
        String firstName = fields.get("FirstName").getContent();
        String lastName = fields.get("LastName").getContent();
        String birthDate = fields.get("DateOfBirth").getContent().replace(".", " ");

        log.info("ID lastName = " + lastName + ", firstName = " + firstName);

        String formFirstName = appUserDto.getFirstName();
        String formLastName = appUserDto.getLastName();

        String dateFormat = identification.equalsIgnoreCase("passport") ? "dd MMM/MMM yyyy" : "dd MM yyyy";

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        String formBirthDate = appUserDto.getBirthDate().format(dateTimeFormatter).replace(".", "");

        log.info("FORM lastName = " + formLastName + ", firstName = " + formFirstName);
        log.info("Form birthdate = " + formBirthDate + ", id birthdate = " + birthDate);

        if (!formFirstName.equalsIgnoreCase(firstName)) {
            String message = String.format("Form's first name '%s' not matching with ID's first name '%s'",
                    formFirstName,
                    firstName
            );
            log.error(message);
            throw new InvalidIdDocumentException(message);
        }

        if (!formLastName.equalsIgnoreCase(lastName)) {
            String message = String.format("Form's last name '%s' not matching with ID's last name '%s'",
                    formLastName,
                    lastName
            );
            log.error(message);
            throw new InvalidIdDocumentException(message);
        }

        if (!formBirthDate.equalsIgnoreCase(birthDate)) {
            String message = String.format("Form's birth date '%s' not matching with ID's birth date '%s'",
                    formBirthDate,
                    birthDate
            );
            log.error(message);
            throw new InvalidIdDocumentException(message);
        }

        if (identification.equalsIgnoreCase("passport")) {
            DocumentField nationality = fields.get("Nationality");
            System.out.println(nationality == null);
            if (nationality == null) {
                throw new InvalidIdDocumentException("Nationality not present or not readable!");
            }
            if (!nationality.getContent().toLowerCase().contains(appUserDto.getNationality().toLowerCase())) {
                String message = String.format("Nationality '%s' on passport does not match with the provided one: '%s'",
                        nationality,
                        appUserDto.getNationality()
                );
                log.error(message);
                throw new InvalidIdDocumentException(message);
            }
        }

        return true;
    }

    private boolean isValidSelfieForId(MultipartFile idDocument, MultipartFile selfiePhoto) {
        return true;
    }
}
