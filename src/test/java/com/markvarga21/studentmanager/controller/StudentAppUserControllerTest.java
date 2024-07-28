package com.markvarga21.studentmanager.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.markvarga21.studentmanager.entity.StudentAppUser;
import com.markvarga21.studentmanager.repository.StudentAppUserRepository;
import com.markvarga21.studentmanager.service.auth.TokenManagementService;
import com.markvarga21.studentmanager.service.auth.webtoken.JwtService;

@WebMvcTest(StudentAppUserController.class)
public class StudentAppUserControllerTest {
        /**
     * The {@code MockMvc} object used for testing the API.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The {@code ReportService} for using the service methods.
     */
    @MockBean
    private StudentAppUserRepository repository;

    /**
     * The {@code JwtService} for mocking the JWT service.
     */
    @MockBean
    private JwtService jwtService;

    /**
     * The {@code TokenManagementService} for mocking the token management.
     */
    @MockBean
    private TokenManagementService tokenManagementService;

    /**
     * The API URL for the report endpoints.
     */
    static final String API_URL = "/api/v1/studentUser";

    @WithMockUser(roles = "USER")
    @Test
    void shouldGetStudentByUsernameTest() throws Exception {
        // Given
        StudentAppUser user = new StudentAppUser();
        user.setId(1L);
        user.setUsername("john12");
        user.setStudentId(2L);

        // When
        when(this.repository.findByUsername(user.getUsername()))
            .thenReturn(Optional.of(user));

        // Then
        this.mockMvc.perform(get(API_URL + "/" + user.getUsername()))
            .andExpect(status().isOk())
            .andExpect(content().string(user.getStudentId().toString()));
    }
}
