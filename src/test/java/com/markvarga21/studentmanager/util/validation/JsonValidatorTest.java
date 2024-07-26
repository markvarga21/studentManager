package com.markvarga21.studentmanager.util.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JsonValidatorTest {
    /**
     * The JSON validator component under testing.
     */
    @InjectMocks
    private JsonValidator jsonValidator;

    /**
     * The JSON schema for mocking the schema.
     */
    @Mock
    private JsonSchema jsonSchema;

    @Test
    void shouldReturnTrueWhenJsonIsValid() {
        // Given
        // When
        doReturn(Set.of())
                .when(this.jsonSchema)
                .validate(any(JsonNode.class));
        boolean actual = this.jsonValidator.isJsonValid(null);

        // Then
        assertTrue(actual);
    }

    @Test
    void shouldReturnFalseWhenJsonIsInvalid() {
        // Given
        ValidationMessage validationMessage = ValidationMessage
                .builder()
                .message("Invalid JSON file")
                .build();
        Set<ValidationMessage> validationMessages = Set.of(validationMessage);

        // When
        when(this.jsonSchema.validate(any()))
                .thenReturn(validationMessages);
        boolean actual = this.jsonValidator.isJsonValid(null);

        // Then
        assertFalse(actual);
    }
}