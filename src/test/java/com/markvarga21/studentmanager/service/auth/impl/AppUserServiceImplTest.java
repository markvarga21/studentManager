package com.markvarga21.studentmanager.service.auth.impl;

import com.markvarga21.studentmanager.dto.Role;
import com.markvarga21.studentmanager.entity.AppUser;
import com.markvarga21.studentmanager.exception.InvalidUserCredentialsException;
import com.markvarga21.studentmanager.exception.UserNotFoundException;
import com.markvarga21.studentmanager.repository.AppUserRepository;
import com.markvarga21.studentmanager.repository.StudentAppUserRepository;
import com.markvarga21.studentmanager.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.markvarga21.studentmanager.data.TestingData.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {
    /**
     * The user service under testing.
     */
    @InjectMocks
    private AppUserServiceImpl service;

    /**
     * The repository for the users.
     */
    @Spy
    private AppUserRepository repository;

    /**
     * The repository for the student application user.
     */
    @Mock
    private StudentService studentService;

    /**
     * The repository for the student application user.
     */
    @Mock
    private StudentAppUserRepository studentAppUserRepository;

    @Test
    void shouldGetUserByUsernameIfPresentTest() {
        // Given
        String username = USER.getUsername();

        // When
        when(this.repository.findByUsername(username))
                .thenReturn(Optional.of(USER));
        Optional<AppUser> user = this.service.getUserByUsername(username);

        // Then
        assertFalse(user.isEmpty());
        assertEquals(USER, user.get());
    }

    @Test
    void shouldGetUserByUsernameIfNotPresentTest() {
        // Given
        String username = USER.getUsername();

        // When
        when(this.repository.findByUsername(anyString()))
                .thenReturn(Optional.empty());
        Optional<AppUser> user = this.service.getUserByUsername(username);

        // Then
        assertTrue(user.isEmpty());
    }

    @Test
    void shouldThrowExceptionUponUserRegistrationIfUsernameIsNotPresentTest() {
        // Given
        // When
        when(this.repository.findByUsername(anyString()))
                .thenReturn(Optional.of(USER));

        // Then
        assertThrows(
                InvalidUserCredentialsException.class,
                () -> this.service.registerUser(USER)
        );
    }

    @Test
    void shouldRegisterUserTest() {
        // Given
        // When
        when(this.repository.findByUsername(anyString()))
                .thenReturn(Optional.empty());
        when(this.repository.save(any()))
                .thenReturn(USER);
        AppUser user = this.service.registerUser(USER);

        // Then
        assertEquals(USER, user);
    }

    @Test
    void shouldFetchAllUsersTest() {
        // Given
        List<AppUser> expected = List.of(USER);
        Page<AppUser> appUserPage = new PageImpl<>(expected);

        // When
        when(this.repository.findAll(any(Pageable.class)))
                .thenReturn(appUserPage);
        List<AppUser> actual = this.service
                .getAllUsers(PAGE, SIZE).getContent();

        // Then
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void shouldDeleteUserIfPresentTest() {
        // Given
        Long userId = USER.getId();
        String expected = String.format(
                "User with username '%s' has been deleted.",
                USER.getUsername()
        );

        // When
        when(this.repository.findById(userId))
                .thenReturn(Optional.of(USER));
        String actual = this.service.deleteUserById(userId);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundUponDeletionTest() {
        // Given
        // When
        when(this.repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                UserNotFoundException.class,
                () -> this.service.deleteUserById(1L)
        );
    }

    @Test
    void shouldThrowExceptionWhenDeletingAdminUserTest() {
        // Given
        AppUser adminUser = new AppUser();
        adminUser.setId(1L);
        adminUser.setRoles(Set.of(Role.ADMIN));

        // When
        when(this.repository.findById(adminUser.getId()))
                .thenReturn(Optional.of(adminUser));

        // Then
        assertThrows(
                InvalidUserCredentialsException.class,
                () -> this.service.deleteUserById(adminUser.getId())
        );
    }

    @Test
    void shouldGetUserByIdIfPresentTest() {
        // Given
        Long userId = USER.getId();

        // When
        when(this.repository.findById(userId))
                .thenReturn(Optional.of(USER));
        AppUser user = this.service.getUserById(userId);

        // Then
        assertEquals(USER, user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundUponGettingByIdTest() {
        // Given
        // When
        when(this.repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                UserNotFoundException.class,
                () -> this.service.getUserById(1L)
        );
    }

    @Test
    void shouldGrantRolesIfUserIsPresentTest() {
        // Given
        String username = USER.getUsername();
        String roles = "ROLE_ADMIN";

        // When
        when(this.repository.findByUsername(username))
                .thenReturn(Optional.of(USER));
        this.service.grantRoles(username, roles);

        // Then
        assertTrue(USER.getRoles().contains(Role.ADMIN));
    }

    @Test
    void shouldThrowExceptionUponRoleGrantingIfUserIsNotPresentTest() {
        // Given
        String username = USER.getUsername();
        String roles = "ROLE_ADMIN";

        // When
        when(this.repository.findByUsername(username))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                UserNotFoundException.class,
                () -> this.service.grantRoles(username, roles)
        );
    }

    @Test
    void shouldRevokeRolesIfUserIsPresentTest() {
        // Given
        String username = USER.getUsername();
        String roles = "ROLE_ADMIN";

        // When
        when(this.repository.findByUsername(username))
                .thenReturn(Optional.of(USER));
        this.service.revokeRoles(username, roles);

        // Then
        assertFalse(USER.getRoles().contains(Role.ADMIN));
    }

    @Test
    void shouldThrowExceptionUponRoleRevokingIfUserIsNotPresentTest() {
        // Given
        String username = USER.getUsername();
        String roles = "ROLE_ADMIN";

        // When
        when(this.repository.findByUsername(username))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                UserNotFoundException.class,
                () -> this.service.revokeRoles(username, roles)
        );
    }
}