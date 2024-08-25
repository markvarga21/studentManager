package com.markvarga21.studentmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.markvarga21.studentmanager.entity.PassportValidationData;
import com.markvarga21.studentmanager.service.auth.webtoken.JwtService;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.service.validation.passport.PassportValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.markvarga21.studentmanager.data.TestingData.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PassportValidationController.class)
class PassportValidationControllerTest {
    /**
     * The {@code MockMvc} object used for testing the API.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The {@code PassportValidationService} for mocking the passport
     * validation service.
     */
    @MockBean
    private PassportValidationService passportValidationService;

    /**
     * The {@code FormRecognizerService} for mocking the form
     * recognizer service.
     */
    @MockBean
    private FormRecognizerService formRecognizerService;

    /**
     * The {@code JwtService} for mocking the JWT service.
     */
    @MockBean
    private JwtService jwtService;

    /**
     * The URL used for testing the API.
     */
    static final String API_URL = "/api/v1/validations";

    /**
     * The {@code ObjectMapper} used for converting objects to JSON.
     */
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldReturnAllPassportValidationData() throws Exception {
        // Given
        Page<PassportValidationData> passportValidationDataPage = new PageImpl<>(List.of(
                PASSPORT_VALIDATION_DATA1,
                PASSPORT_VALIDATION_DATA2
        ));

        // When
        when(this.passportValidationService.getAllPassportValidationData(PAGE, SIZE))
                .thenReturn(passportValidationDataPage);

        // Then
        this.mockMvc.perform(get(API_URL)
                        .param("page", PAGE.toString())
                        .param("size", SIZE.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*]", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].timestamp").value("2000-01-01T00:00:00"))
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.content[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.content[0].birthDate").value("2001-02-02"))
                .andExpect(jsonPath("$.content[0].placeOfBirth").value("New York"))
                .andExpect(jsonPath("$.content[0].countryOfCitizenship").value("USA"))
                .andExpect(jsonPath("$.content[0].gender").value("MALE"))
                .andExpect(jsonPath("$.content[0].passportNumber").value("123456789"))
                .andExpect(jsonPath("$.content[0].passportDateOfIssue").value("2022-03-03"))
                .andExpect(jsonPath("$.content[0].passportDateOfExpiry").value("2023-04-04"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testManualValidation() throws Exception {
        // Given
        Long studentId = 1L;
        String successMessage = "Manually validating passport for user with ID '1'";

        // When
        when(this.formRecognizerService.validatePassportManually(studentId))
                .thenReturn(successMessage);

        // Then
        this.mockMvc.perform(post(API_URL + "/validateManually?studentId=1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(successMessage));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldReturnValidIfStudentIsValid() throws Exception {
        // Given
        String passportNumber = "123456789";
        boolean isValid = true;

        // When
        when(this.formRecognizerService.isUserValid(passportNumber))
                .thenReturn(isValid);

        // Then
        this.mockMvc.perform(get(API_URL + "/isUserValid/" + passportNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(isValid));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldReturnInvalidIfStudentIsInvalid() throws Exception {
        // Given
        boolean isValid = false;

        // When
        when(this.formRecognizerService.isUserValid(anyString()))
                .thenReturn(isValid);

        // Then
        this.mockMvc.perform(get(API_URL + "/isUserValid/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(isValid));
    }

    @WithMockUser(roles = "USER")
    @Test
    void shouldCreatePassportValidationData() throws Exception {
        when(this.passportValidationService.createPassportValidationData(any()))
                .thenReturn(PASSPORT_VALIDATION_DATA1);

        // make a string out of it and check those shortly
        this.mockMvc.perform(post(API_URL).with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(PASSPORT_VALIDATION_DATA1)))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.timestamp").value("2000-01-01T00:00:00"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("2001-02-02"))
                .andExpect(jsonPath("$.placeOfBirth").value("New York"))
                .andExpect(jsonPath("$.countryOfCitizenship").value("USA"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.passportNumber").value("123456789"))
                .andExpect(jsonPath("$.passportDateOfIssue").value("2022-03-03"))
                .andExpect(jsonPath("$.passportDateOfExpiry").value("2023-04-04"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldFetchPassportValidationByPassportNumber() throws Exception {
        // Given
        String passportNumber = "123456789";

        // When
        when(this.passportValidationService.getStudentFromPassportValidation(passportNumber))
                .thenReturn(STUDENT_DTO);

        // Then
        this.mockMvc.perform(get(API_URL + "/" + passportNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("2001-02-02"))
                .andExpect(jsonPath("$.placeOfBirth").value("New York"))
                .andExpect(jsonPath("$.countryOfCitizenship").value("USA"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.passportNumber").value("123456"))
                .andExpect(jsonPath("$.passportDateOfIssue").value("2020-01-01"))
                .andExpect(jsonPath("$.passportDateOfExpiry").value("2025-01-01"));
    }
}