package com.markvarga21.studentmanager.util.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * A utility class which is used to validate JSON content.
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
     * Validates the JSON content.
     *
     * @param json The JSON content to be validated.
     * @return Whether the JSON content is valid or not.
     */
    public boolean isJsonValid(final JsonNode json) {
        Set<ValidationMessage> validationMessages = this.jsonSchema
                .validate(json);
        if (!validationMessages.isEmpty()) {
            log.error("Invalid JSON content: " + validationMessages);
            return false;
        }
        return true;
    }
}
