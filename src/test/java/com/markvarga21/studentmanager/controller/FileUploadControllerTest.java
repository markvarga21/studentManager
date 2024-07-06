package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.StudentImage;
import com.markvarga21.studentmanager.service.StudentService;
import com.markvarga21.studentmanager.service.auth.webtoken.JwtService;
import com.markvarga21.studentmanager.service.faceapi.FaceApiService;
import com.markvarga21.studentmanager.service.file.FileUploadService;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.service.validation.passport.PassportValidationService;
import com.markvarga21.studentmanager.util.StudentImageType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileUploadController.class)
class FileUploadControllerTest {
    /**
     * The {@code MockMvc} object used for testing the API.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The {@code FileUploadService} for mocking the file services.
     */
    @MockBean
    private FileUploadService fileUploadService;

    /**
     * The {@code StudentService} for mocking the student services.
     */
    @MockBean
    private StudentService studentService;

    /**
     * The {@code FaceApiService} for mocking the face API services.
     */
    @MockBean
    private FaceApiService faceApiService;

    /**
     * The {@code FormRecognizerService} for mocking the form recognizer services.
     */
    @MockBean
    private FormRecognizerService formRecognizerService;

    /**
     * The {@code PassportValidationService} for mocking the passport validation services.
     */
    @MockBean
    private PassportValidationService passportValidationService;

    /**
     * The {@code JwtService} for mocking the JWT service.
     */
    @MockBean
    private JwtService jwtService;

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldFetchAllImagesTest() throws Exception {
        // Given
        StudentImage studentImage = new StudentImage();
        studentImage.setStudentId(1L);
        studentImage.setSelfieImage("selfieImage".getBytes());
        studentImage.setPassportImage("passportImage".getBytes());

        StudentImage studentImage2 = new StudentImage();
        studentImage.setStudentId(2L);
        studentImage.setSelfieImage("selfieImage2".getBytes());
        studentImage.setPassportImage("passportImage2".getBytes());

        // When
        when(this.fileUploadService.getAllImages())
                .thenReturn(List.of(studentImage, studentImage2));


        // Then
        this.mockMvc.perform(get("/api/v1/files"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldDeleteImageForStudentIdTest() throws Exception {
        // Given
        Long studentId = 1L;

        // Then
        when(this.fileUploadService.deleteImage(studentId))
                .thenReturn(String.format("Image deleted successfully for user '%d'", studentId));

        // Then
        this.mockMvc.perform(delete(String.format("/api/v1/files/%d", studentId)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$")
                        .value(String.format("Image deleted successfully for user '%d'", studentId)));
    }

    @WithMockUser(roles = "USER")
    @Test
    void shouldGetImageForTypeTest() throws Exception {
        // Given
        Long studentId = 1L;
        StudentImageType type = StudentImageType.PASSPORT;
        byte[] imageBytes = "passportImage".getBytes();
        String imageString = new String(imageBytes, StandardCharsets.UTF_8);

        // When
        when(this.fileUploadService.getImageForType(studentId, type))
                .thenReturn(imageBytes);

        // Then
        this.mockMvc.perform(get(String.format("/api/v1/files/%d?imageType=%s", studentId, type)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(imageString)));
    }

    @WithMockUser(roles = "USER")
    @Test
    void shouldUploadImageTest() throws Exception {
        // Given
        Long studentId = 1L;
        MockMultipartFile passport = new MockMultipartFile(
                "passport",
                "passport.jpeg",
                "image/jpeg",
                "passportImage".getBytes()
        );
        MockMultipartFile selfie = new MockMultipartFile(
                "selfie",
                "selfie.jpeg",
                "image/jpeg",
                "selfieImage".getBytes()
        );
        String expected = String
                .format("Images saved successfully for user '%s'", studentId);

        // When
        when(this.fileUploadService.uploadFile(studentId, passport, selfie))
                .thenReturn(expected);

        // Then
        this.mockMvc.perform(multipart(String.format("/api/v1/files/upload/%d", studentId))
                .file(passport)
                .file(selfie).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$")
                        .value(String.format("Images saved successfully for user '%s'", studentId)));
    }

    @WithMockUser(roles = "USER")
    @Test
    void shouldChangePassportImageTest() throws Exception {
        // Given
        Long studentId = 1L;
        StudentImageType imageType = StudentImageType.PASSPORT;
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "passport.jpeg",
                "image/jpeg",
                "passportImage".getBytes()
        );
        String expected = String
                .format("Passport image changed successfully for user '%s'", studentId);
        StudentDto studentDto = new StudentDto();
        studentDto.setPassportNumber("123456");

        // When
        when(this.studentService.getStudentById(studentId))
                .thenReturn(studentDto);
        when(this.formRecognizerService.extractDataFromPassport(file))
                .thenReturn(studentDto);

        // Then
        this.mockMvc.perform(multipart(String.format("/api/v1/files/changeImage/%d/%s", studentId, imageType))
                .file(file).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$")
                        .value(expected));
        verify(this.studentService, times(1))
                .setValidity(studentId, false);
        verify(this.passportValidationService)
                .deletePassportValidationData(anyString());
        verify(this.passportValidationService)
                .createPassportValidationData(any());
    }

    @WithMockUser(roles = "USER")
    @Test
    void shouldChangeSelfieImageTest() throws Exception {
        // Given
        Long studentId = 1L;
        StudentImageType imageType = StudentImageType.SELFIE;
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "passport.jpeg",
                "image/jpeg",
                "selfieImage".getBytes()
        );
        String expected = String
                .format("Selfie image changed successfully for user '%s'", studentId);
        StudentDto studentDto = new StudentDto();
        studentDto.setPassportNumber("123456");

        // When
        when(this.studentService.getStudentById(studentId))
                .thenReturn(studentDto);
        when(this.formRecognizerService.extractDataFromPassport(file))
                .thenReturn(studentDto);

        // Then
        this.mockMvc.perform(multipart(String.format("/api/v1/files/changeImage/%d/%s", studentId, imageType))
                        .file(file).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$")
                        .value(expected));
        verify(this.studentService, times(1))
                .setValidity(studentId, false);
        verify(this.faceApiService)
                .deleteFace(anyString());
    }
}