package com.markvarga21.studentmanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.markvarga21.studentmanager.service.auth.webtoken.JwtService;
import com.markvarga21.studentmanager.util.validation.JsonValidator;
import com.markvarga21.studentmanager.util.validation.XmlValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static com.markvarga21.studentmanager.data.TestingData.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExportValidatorController.class)
class ExportValidatorControllerTest {
    /**
     * The {@code MockMvc} object used for testing the API.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The {@code XmlValidator} for mocking the XML validation.
     */
    @MockBean
    private XmlValidator xmlValidator;

    /**
     * The {@code JsonValidator} for mocking the JSON validation.
     */
    @MockBean
    private JsonValidator jsonValidator;

    /**
     * The {@code JwtService} for mocking the JWT service.
     */
    @MockBean
    private JwtService jwtService;

    /**
     * The {@code ObjectMapper} for JSON operations.
     */
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldReturnTrueWhenXmlIsValidTest() throws Exception {
        // Given
        // When
        when(this.xmlValidator.isXmlValid(VALID_XML_STUDENT))
                .thenReturn(true);
        // Then
        this.mockMvc.perform(post("/api/v1/export/validate/xml")
                        .contentType("application/xml")
                        .content(VALID_XML_STUDENT)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldReturnFalseWhenXmlIsInvalidTest() throws Exception {
        // Given
        // When
        when(this.xmlValidator.isXmlValid(INVALID_XML_STUDENT))
                .thenReturn(false);
        // Then
        this.mockMvc.perform(post("/api/v1/export/validate/xml")
                        .contentType("application/xml")
                        .content(INVALID_XML_STUDENT)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andReturn();
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldThrowExceptionUponXmlValidationTest() throws Exception {
        // Given
        // When
        when(this.xmlValidator.isXmlValid(INVALID_XML_STUDENT))
                .thenThrow(new RuntimeException("Invalid XML file"));
        // Then
        this.mockMvc.perform(post("/api/v1/export/validate/xml")
                        .contentType("application/xml")
                        .content(INVALID_XML_STUDENT)
                        .with(csrf()))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("false"))
                .andReturn();
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldReturnTrueWhenJsonIsValidTest() throws Exception {
        // Given
        JsonNode jsonNode = this.objectMapper
                        .readTree(VALID_JSON_STUDENT);

        // When
        when(this.jsonValidator.isJsonValid(jsonNode))
                .thenReturn(true);
        // Then
        this.mockMvc.perform(post("/api/v1/export/validate/json")
                        .contentType("application/json")
                        .content(VALID_JSON_STUDENT)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldReturnFalseWhenJsonIsInvalidTest() throws Exception {
        // Given
        JsonNode jsonNode = this.objectMapper
                .readTree(INVALID_JSON_STUDENT);

        // When
        when(this.jsonValidator.isJsonValid(jsonNode))
                .thenReturn(false);
        // Then
        this.mockMvc.perform(post("/api/v1/export/validate/json")
                        .contentType("application/json")
                        .content(INVALID_JSON_STUDENT)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andReturn();
    }

    @WithMockUser
    @Test
    void shouldThrowExceptionUponJsonValidationTest() throws Exception {
        // Given
        JsonNode jsonNode = this.objectMapper
                .readTree(INVALID_JSON_STUDENT);

        // When
        when(this.jsonValidator.isJsonValid(jsonNode))
                .thenThrow(new RuntimeException("Invalid JSON file"));
        // Then
        this.mockMvc.perform(post("/api/v1/export/validate/json")
                        .contentType("application/json")
                        .content(INVALID_JSON_STUDENT)
                        .with(csrf()))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("false"))
                .andReturn();
    }

}