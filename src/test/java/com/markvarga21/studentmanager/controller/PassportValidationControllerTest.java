package com.markvarga21.studentmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.Gender;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static com.markvarga21.studentmanager.data.TestingData.PAGE;
import static com.markvarga21.studentmanager.data.TestingData.SIZE;
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

    /**
     * The first passport validation data used for testing.
     */
    static final PassportValidationData PASSPORT_VALIDATION_DATA1 = new PassportValidationData(
            1L,
            LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0),
            "John",
            "Doe",
            LocalDate.of(2001, 2, 2),
            "New York",
            "USA",
            Gender.MALE,
            "123456789",
            LocalDate.of(2023, 4, 4),
            LocalDate.of(2022, 3, 3)
    );

    /**
     * The second passport validation data used for testing.
     */
    static final PassportValidationData PASSPORT_VALIDATION_DATA2 = new PassportValidationData(
            2L,
            LocalDateTime.of(1980, Month.JANUARY, 1, 0, 0),
            "John",
            "Wick",
            LocalDate.of(2001, 2, 2),
            "Washington",
            "USA",
            Gender.MALE,
            "23456789",
            LocalDate.of(2022, 3, 3),
            LocalDate.of(2023, 4, 4)
    );

    /**
     * Static student data based on the information found in
     * {@code PASSPORT_VALIDATION_DATA1} passport.
     */
    static final StudentDto STUDENT_DTO = StudentDto.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .birthDate("2001-02-02")
            .placeOfBirth("New York")
            .countryOfCitizenship("USA")
            .gender(Gender.MALE)
            .passportNumber("123456789")
            .passportDateOfIssue("2022-03-03")
            .passportDateOfExpiry("2023-04-04")
            .build();

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
                .andExpect(jsonPath("$.passportNumber").value("123456789"))
                .andExpect(jsonPath("$.passportDateOfIssue").value("2022-03-03"))
                .andExpect(jsonPath("$.passportDateOfExpiry").value("2023-04-04"));
    }
}