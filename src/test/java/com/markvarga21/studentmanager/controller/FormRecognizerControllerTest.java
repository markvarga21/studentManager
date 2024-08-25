package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.Gender;
import com.markvarga21.studentmanager.service.auth.webtoken.JwtService;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FormRecognizerController.class)
class FormRecognizerControllerTest {
    /**
     * The {@code MockMvc} object used for testing the API.
     */
    @Autowired
    private MockMvc mockMvc;

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

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldGetDataFromPassport() throws Exception {
        // Given
        StudentDto studentDto = new StudentDto(
                1L,
                "John",
                "Doe",
                "2001-01-01",
                "New York",
                "USA",
                Gender.MALE,
                "123456",
                "2030-01-01",
                "2020-01-01",
                true
        );
        MockMultipartFile file = new MockMultipartFile(
                "passport",
                "passport.jpeg",
                "image/jpeg",
                "mock file".getBytes()
        );

        // When
        when(this.formRecognizerService.extractDataFromPassport(any()))
                .thenReturn(studentDto);

        // Then
        this.mockMvc.perform(multipart("/api/v1/form/extractData")
                .file(file).with(csrf())
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(studentDto.getId()))
                .andExpect(jsonPath("$.firstName")
                        .value(studentDto.getFirstName()))
                .andExpect(jsonPath("$.lastName")
                        .value(studentDto.getLastName()))
                .andExpect(jsonPath("$.birthDate")
                        .value(studentDto.getBirthDate()))
                .andExpect(jsonPath("$.placeOfBirth")
                        .value(studentDto.getPlaceOfBirth()))
                .andExpect(jsonPath("$.countryOfCitizenship")
                        .value(studentDto.getCountryOfCitizenship()))
                .andExpect(jsonPath("$.gender")
                        .value(studentDto.getGender().name()))
                .andExpect(jsonPath("$.passportNumber")
                        .value(studentDto.getPassportNumber()))
                .andExpect(jsonPath("$.passportDateOfIssue")
                        .value(studentDto.getPassportDateOfIssue()))
                .andExpect(jsonPath("$.passportDateOfExpiry")
                        .value(studentDto.getPassportDateOfExpiry()))
                .andExpect(jsonPath("$.valid")
                        .value(studentDto.isValid()));
    }

}