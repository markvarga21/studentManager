package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.service.auth.webtoken.JwtService;
import com.markvarga21.studentmanager.service.faceapi.FaceApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.markvarga21.studentmanager.data.TestingData.FACE_API_RESPONSE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FaceController.class)
class FaceControllerTest {
    /**
     * The {@code MockMvc} object used for testing the API.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The {@code FaceApiService} for mocking the face
     * API service.
     */
    @MockBean
    private FaceApiService faceApiService;

    /**
     * The {@code JwtService} for mocking the JWT service.
     */
    @MockBean
    private JwtService jwtService;

    /**
     * A simple mock image.
     */
    static final String IMAGE_PATH = "images/test-image-for-compression.jpg";

    /**
     * The selfie image.
     */
    private MockMultipartFile selfieImage;

    /**
     * The passport image.
     */
    private MockMultipartFile passportImage;

    @BeforeEach
    void setUp() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(IMAGE_PATH).getFile());
        byte[] fileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "test-image-for-compression.jpg",
                "image/jpeg",
                fileBytes
        );
        this.selfieImage = mockMultipartFile;
        this.passportImage = mockMultipartFile;
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldFetchSelfieValidationDataTest() throws Exception {
        // Given
        when(this.faceApiService.getValidityOfFaces(
                any(MultipartFile.class), any(MultipartFile.class)
        )).thenReturn(FACE_API_RESPONSE);

        // When
        // Then
        this.mockMvc.perform(multipart("/api/v1/faces/validate")
                .file("passport", this.passportImage.getBytes())
                .file("selfiePhoto", this.selfieImage.getBytes()).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isIdentical")
                        .value(FACE_API_RESPONSE.getIsIdentical()))
                .andExpect(jsonPath("$.confidence")
                        .value(FACE_API_RESPONSE.getConfidence()));
    }
}