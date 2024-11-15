package com.markvarga21.studentmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.markvarga21.studentmanager.dto.Role;
import com.markvarga21.studentmanager.dto.UserLogin;
import com.markvarga21.studentmanager.entity.AppUser;
import com.markvarga21.studentmanager.service.auth.AppUserService;
import com.markvarga21.studentmanager.service.auth.webtoken.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static com.markvarga21.studentmanager.data.TestingData.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppUserController.class)
class AppUserControllerTest {
    /**
     * The {@code MockMvc} object used for testing the API.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The {@code AppUserService} for mocking the user service.
     */
    @MockBean
    private AppUserService appUserService;

    /**
     * The {@code AuthenticationManager} for mocking the authentication manager.
     */
    @MockBean
    private AuthenticationManager authenticationManager;

    /**
     * The {@code JwtService} for mocking the JWT service.
     */
    @MockBean
    private JwtService jwtService;

    /**
     * The {@code UserDetailsService} for mocking the user details service.
     */
    @MockBean
    private UserDetailsService userDetailsService;

    /**
     * The {@code UserDetails} for mocking the user details.
     */
    @MockBean
    private UserDetails userDetails;

    /**
     * The password encoder.
     */
    @MockBean
    private PasswordEncoder passwordEncoder;

    /**
     * The authentication mock bean.
     */
    @MockBean
    private Authentication authentication;

    /**
     * The LogoutSuccessHandler object.
     */
    @MockBean
    private LogoutSuccessHandler logoutSuccessHandler;

    /**
     * The URL used for testing the API.
     */
    static final String API_URL = "/api/v1/auth";

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldFetchAllUsersTest() throws Exception {
        // Given
        Page<AppUser> users = new PageImpl<>(List.of(USER));

        // When
        when(this.appUserService.getAllUsers(PAGE, SIZE))
                .thenReturn(users);

        // Then
        this.mockMvc.perform(get(API_URL + "/users")
                        .param("page", PAGE.toString())
                        .param("size", SIZE.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*]", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(USER.getId()))
                .andExpect(jsonPath("$.content[0].username").value(USER.getUsername()))
                .andExpect(jsonPath("$.content[0].email").value(USER.getEmail()))
                .andExpect(jsonPath("$.content[0].password").value(USER.getPassword()))
                .andExpect(jsonPath("$.content[0].firstName").value(USER.getFirstName()))
                .andExpect(jsonPath("$.content[0].lastName").value(USER.getLastName()))
                .andExpect(jsonPath("$.content[0].roles").value(Role.USER.name()));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldDeleteUserTest() throws Exception {
        // Given
        Long studentId = USER.getId();
        String expected = String.format(
                "User with username '%s' has been deleted.",
                studentId
        );

        // When
        when(this.appUserService.deleteUserById(studentId))
                .thenReturn(expected);

        // Then
        this.mockMvc.perform(delete(API_URL + "/users/" + studentId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(expected));
    }

    @Test
    void shouldThrowExceptionUponRegistrationIfInvalidTest() throws Exception {
        // Given
        AppUser appUser = USER;
        appUser.setRoles(Set.of(Role.USER));
        ObjectMapper objectMapper = new ObjectMapper();

        // When
        // Then
        this.mockMvc.perform(post(API_URL + "/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(appUser))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginUserWithInvalidCredentialsShouldReturnUnauthorized() throws Exception {
        // Given
        UserLogin invalidUserLogin = new UserLogin("invalidUser", "invalidPassword");
        ObjectMapper objectMapper = new ObjectMapper();

        // When
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid user credentials"));

        // Then
        mockMvc.perform(post(API_URL + "/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidUserLogin))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldGetUserByIdTest() throws Exception {
        // Given
        Long userId = USER.getId();

        // When
        when(this.appUserService.getUserById(userId))
                .thenReturn(USER);

        // Then
        this.mockMvc.perform(get(API_URL + "/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER.getId()))
                .andExpect(jsonPath("$.username").value(USER.getUsername()))
                .andExpect(jsonPath("$.email").value(USER.getEmail()))
                .andExpect(jsonPath("$.password").value(USER.getPassword()))
                .andExpect(jsonPath("$.firstName").value(USER.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(USER.getLastName()))
                .andExpect(jsonPath("$.roles").value(Role.USER.name()));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldGrantRolesTest() throws Exception {
        // Given
        String username = USER.getUsername();
        String roles = "ROLE_ADMIN,ROLE_USER";
        String message = String.format(
                "Roles %s granted to user %s.",
                roles,
                username
        );

        // When
        when(this.appUserService.grantRoles(username, roles))
                .thenReturn(message);

        // Then
        this.mockMvc.perform(put(API_URL + "/users/grant")
                        .param("username", username)
                        .param("roles", roles)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(message));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldRevokeRolesTest() throws Exception {
        // Given
        String username = USER.getUsername();
        String roles = "ROLE_ADMIN,ROLE_USER";
        String message = String.format(
                "Roles %s revoked from user %s.",
                roles,
                username
        );

        // When
        when(this.appUserService.revokeRoles(username, roles))
                .thenReturn(message);

        // Then
        this.mockMvc.perform(put(API_URL + "/users/revoke")
                        .param("username", username)
                        .param("roles", roles)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(message));
    }
}