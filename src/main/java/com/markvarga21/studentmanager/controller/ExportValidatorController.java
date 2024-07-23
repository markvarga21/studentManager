package com.markvarga21.studentmanager.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.markvarga21.studentmanager.util.validation.JsonValidator;
import com.markvarga21.studentmanager.util.validation.XmlValidator;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The ExportValidatorController class is used to validate exported
 * XML and JSON data.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/export")
@CrossOrigin
@Slf4j
public class ExportValidatorController {
    /**
     * The invalid data status code.
     */
    private static final Integer INVALID_DATA = 500;
    /**
     * The XML validator.
     */
    private final XmlValidator xmlValidator;

    /**
     * The JSON validator.
     */
    private final JsonValidator jsonValidator;

    /**
     * Validates the exported XML data.
     *
     * @param xml The exported XML data.
     * @return The validation result.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/validate/xml")
    @Operation(summary = "Validates an XML document.")
    public ResponseEntity<Boolean> validateXmlExport(
            @RequestBody final String xml
    ) {
        log.info("Validating XML data.");
        try {
            return ResponseEntity.ok(this.xmlValidator.isXmlValid(xml));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INVALID_DATA)
                    .body(false);
        }
    }

    /**
     * Validates the exported JSON data.
     *
     * @param json The exported JSON data.
     * @return The validation result.
     */
    @PostMapping("/validate/json")
    @Operation(summary = "Validates a JSON document.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> validateJsonExport(@RequestBody final JsonNode json) {
        try {
            return ResponseEntity.ok(this.jsonValidator.isJsonValid(json));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INVALID_DATA)
                    .body(false);
        }
    }
}
