package com.markvarga21.studentmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.Gender;
import com.markvarga21.studentmanager.service.StudentService;
import com.markvarga21.studentmanager.service.file.FileUploadService;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.service.validation.face.FacialValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {
    /**
     * The {@code MockMvc} object used for testing the API.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The {@code StudentService} for using the service methods.
     */
    @MockBean
    private StudentService studentService;

    /**
     * The {@code FormRecognizerService} for form related usages.
     */
    @MockBean
    private FormRecognizerService formRecognizerService;

    /**
     * The {@code StudentRepository} for file manipulations.
     */
    @MockBean
    private FileUploadService fileUploadService;

    /**
     * The {@code FacialValidationService} for facial recognition.
     */
    @MockBean
    private FacialValidationService facialValidationService;

    /**
     * The URL used for testing the API.
     */
    static final String API_URL = "/api/v1/students";

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * A {@code StudentDto} used for testing the API.
     */
    private StudentDto studentDto = StudentDto.builder()
            .id(1L)
            .firstName("John").lastName("Doe")
            .birthDate("2000-01-01")
            .placeOfBirth("New York")
            .countryOfCitizenship("USA")
            .gender(Gender.MALE)
            .passportNumber("123456")
            .passportDateOfIssue("2020-01-01")
            .passportDateOfExpiry("2025-01-01")
            .build();
    @Test
    void shouldReturnAllStudentsTest() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(
                studentDto,
                StudentDto.builder()
                        .id(2L)
                        .firstName("John").lastName("Wick")
                        .birthDate("2002-01-01")
                        .placeOfBirth("Washington")
                        .countryOfCitizenship("USA")
                        .gender(Gender.MALE)
                        .passportNumber("123455")
                        .passportDateOfIssue("2021-01-01")
                        .passportDateOfExpiry("2028-01-01")
                        .build()
        ));

        this.mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].birthDate").value("2000-01-01"))
                .andExpect(jsonPath("$[0].placeOfBirth").value("New York"))
                .andExpect(jsonPath("$[0].countryOfCitizenship").value("USA"))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[0].passportNumber").value("123456"))
                .andExpect(jsonPath("$[0].passportDateOfIssue").value("2020-01-01"))
                .andExpect(jsonPath("$[0].passportDateOfExpiry").value("2025-01-01"));
    }

    @Test
    void shouldCreateStudentTest() throws Exception {
        when(this.studentService.validPassportNumber(anyString()))
                .thenReturn(true);

        when(this.studentService.createStudent(studentDto))
                .thenReturn(studentDto);

        this.mockMvc.perform(post(API_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("2000-01-01"))
                .andExpect(jsonPath("$.placeOfBirth").value("New York"))
                .andExpect(jsonPath("$.countryOfCitizenship").value("USA"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.passportNumber").value("123456"))
                .andExpect(jsonPath("$.passportDateOfIssue").value("2020-01-01"))
                .andExpect(jsonPath("$.passportDateOfExpiry").value("2025-01-01"));
    }

    @Test
    void shouldReturnStudentByIdTest() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(studentDto);

        this.mockMvc.perform(get(API_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("2000-01-01"))
                .andExpect(jsonPath("$.placeOfBirth").value("New York"))
                .andExpect(jsonPath("$.countryOfCitizenship").value("USA"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.passportNumber").value("123456"))
                .andExpect(jsonPath("$.passportDateOfIssue").value("2020-01-01"))
                .andExpect(jsonPath("$.passportDateOfExpiry").value("2025-01-01"));
    }

    @Test
    void shouldUpdateStudentByIdTest() throws Exception {
        when(this.studentService.modifyStudentById(studentDto, 1L))
                .thenReturn(studentDto);

        this.mockMvc.perform(put(API_URL + "/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("2000-01-01"))
                .andExpect(jsonPath("$.placeOfBirth").value("New York"))
                .andExpect(jsonPath("$.countryOfCitizenship").value("USA"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.passportNumber").value("123456"))
                .andExpect(jsonPath("$.passportDateOfIssue").value("2020-01-01"))
                .andExpect(jsonPath("$.passportDateOfExpiry").value("2025-01-01"));
    }

    @Test
    void shouldDeleteStudentById() throws Exception {
        when(this.studentService.deleteStudentById(1L))
                .thenReturn(studentDto);

        this.mockMvc.perform(delete(API_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("2000-01-01"))
                .andExpect(jsonPath("$.placeOfBirth").value("New York"))
                .andExpect(jsonPath("$.countryOfCitizenship").value("USA"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.passportNumber").value("123456"))
                .andExpect(jsonPath("$.passportDateOfIssue").value("2020-01-01"))
                .andExpect(jsonPath("$.passportDateOfExpiry").value("2025-01-01"));
    }
}