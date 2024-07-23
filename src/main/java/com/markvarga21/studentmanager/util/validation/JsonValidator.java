package com.markvarga21.studentmanager.util.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * A utility class which is used to validate JSON files.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JsonValidator {
    /**
     * The JSON schema.
     */
    private final JsonSchema jsonSchema;

    /**
     * Validates the JSON file.
     *
     * @param json The JSON file to be validated.
     * @return Whether the JSON file is valid.
     */
    public boolean isJsonValid(final JsonNode json) {
        Set<ValidationMessage> validationMessages = this.jsonSchema
                .validate(json);
        if (!validationMessages.isEmpty()) {
            log.error("Invalid JSON file: " + validationMessages);
            return false;
        }
        return true;
    }
}
