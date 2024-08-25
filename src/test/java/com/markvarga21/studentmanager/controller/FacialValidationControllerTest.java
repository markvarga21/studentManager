package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.entity.FacialValidationData;
import com.markvarga21.studentmanager.service.auth.webtoken.JwtService;
import com.markvarga21.studentmanager.service.validation.face.FacialValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.markvarga21.studentmanager.data.TestingData.PAGE;
import static com.markvarga21.studentmanager.data.TestingData.SIZE;
import static com.markvarga21.studentmanager.data.TestingData.VALID_FACIAL_VALIDATION_DATA;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacialValidationController.class)
class FacialValidationControllerTest {
    /**
     * The {@code MockMvc} object used for testing the API.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The {@code FacialValidationService} for mocking the facial
     * validation service.
     */
    @MockBean
    private FacialValidationService facialValidationService;

    /**
     * The {@code JwtService} for mocking the JWT service.
     */
    @MockBean
    private JwtService jwtService;

    /**
     * The URL used for testing the API.
     */
    static final String API_URL = "/api/v1/facialValidations";

    @WithMockUser(roles = "USER")
    @Test
    void shouldFetchAllFacialValidationDataTest() throws Exception {
        // Given
        Page<FacialValidationData> facialValidationDataPage = new PageImpl<>(
                List.of(VALID_FACIAL_VALIDATION_DATA)
        );

        // When
        when(this.facialValidationService.getAllFacialValidationData(PAGE, SIZE))
                .thenReturn(facialValidationDataPage);

        // Then
        this.mockMvc.perform(get(API_URL)
                        .param("page", PAGE.toString())
                        .param("size", SIZE.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id")
                        .value(VALID_FACIAL_VALIDATION_DATA.getId()))
                .andExpect(jsonPath("$.content[0].passportNumber")
                        .value(VALID_FACIAL_VALIDATION_DATA.getPassportNumber()))
                .andExpect(jsonPath("$.content[0].isValid")
                        .value(VALID_FACIAL_VALIDATION_DATA.getIsValid()))
                .andExpect(jsonPath("$.content[0].percentage")
                        .value(VALID_FACIAL_VALIDATION_DATA.getPercentage()));
    }

    @WithMockUser(roles = "USER")
    @Test
    void shouldFetchFacialValidationDataByPassportNumberTest() throws Exception {
        // Given
        // When
        when(this.facialValidationService.getFacialValidationDataByPassportNumber(anyString()))
                .thenReturn(VALID_FACIAL_VALIDATION_DATA);

        // Then
        this.mockMvc.perform(get(API_URL + "/{passportNumber}", VALID_FACIAL_VALIDATION_DATA.getPassportNumber()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(VALID_FACIAL_VALIDATION_DATA.getId()))
                .andExpect(jsonPath("$.passportNumber")
                        .value(VALID_FACIAL_VALIDATION_DATA.getPassportNumber()))
                .andExpect(jsonPath("$.isValid")
                        .value(VALID_FACIAL_VALIDATION_DATA.getIsValid()))
                .andExpect(jsonPath("$.percentage")
                        .value(VALID_FACIAL_VALIDATION_DATA.getPercentage()));
    }

    @WithMockUser(roles = "USER")
    @Test
    void shouldThrowExceptionUponFacialDataFetchIfMissingTest() throws Exception {
        when(this.facialValidationService.getFacialValidationDataByPassportNumber(anyString()))
                .thenReturn(null);
        this.mockMvc.perform(get(API_URL + "/{passportNumber}", VALID_FACIAL_VALIDATION_DATA.getPassportNumber()))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldDeleteFacialValidationDataTest() throws Exception {
        // Given
        String expected = String.format(
                "Facial validation data for passport number '%s' deleted successfully!",
                VALID_FACIAL_VALIDATION_DATA.getPassportNumber()
        );

        // When
        when(this.facialValidationService.deleteFacialValidationDataByPassportNumber(
            VALID_FACIAL_VALIDATION_DATA.getPassportNumber()))
                .thenReturn(expected);

        // Then
        this.mockMvc.perform(delete(API_URL + "/{passportNumber}",
                        VALID_FACIAL_VALIDATION_DATA
                                .getPassportNumber())
                        .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(expected));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldSetFacialValidationDataToValidTest() throws Exception {
        // Given
        String expected = String.format(
                "Facial validation data for passport number '%s' set to valid!",
                VALID_FACIAL_VALIDATION_DATA.getPassportNumber()
        );

        // When
        when(this.facialValidationService.setFacialValidationToValid(
            VALID_FACIAL_VALIDATION_DATA.getPassportNumber()))
                .thenReturn(expected);

        // Then
        this.mockMvc.perform(post(
                API_URL
                        + "/setFacialValidationDataToValid?passportNumber="
                        + VALID_FACIAL_VALIDATION_DATA.getPassportNumber())
                        .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(expected));
    }
}