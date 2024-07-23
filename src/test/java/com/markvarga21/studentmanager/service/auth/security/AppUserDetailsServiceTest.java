package com.markvarga21.studentmanager.service.auth.security;

import com.markvarga21.studentmanager.dto.Role;
import com.markvarga21.studentmanager.entity.AppUser;
import com.markvarga21.studentmanager.service.auth.AppUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static com.markvarga21.studentmanager.data.TestingData.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceTest {
    /**
     * The {@code AppUserDetailsService} object under testing.
     */
    @InjectMocks
    private AppUserDetailsService appUserDetailsService;

    /**
     * The {@code AppUserService} for mocking the app user service.
     */
    @Mock
    private AppUserService appUserService;

    @Test
    void shouldLoadUserByUsernameIfPresentTest() {
        // Given
        String username = "john12";
        AppUser appUser = USER;

        // When
        when(this.appUserService.getUserByUsername(username))
                .thenReturn(Optional.of(appUser));
        UserDetails userDetails = this.appUserDetailsService
                .loadUserByUsername(username);

        // Then
        assertNotNull(userDetails);
        assertEquals(appUser.getUsername(), userDetails.getUsername());
        assertEquals(appUser.getPassword(), userDetails.getPassword());
        assertArrayEquals(
                appUser
                    .getRoles()
                    .stream()
                    .map(role -> "ROLE_" + role.name())
                    .toArray(),
                userDetails.getAuthorities().stream().map(Object::toString).toArray()
        );
    }

    @Test
    void shouldThrowExceptionUponLoadUserByUsernameIfNotPresentTest() {
        // Given
        String username = "john12";

        // When
        when(this.appUserService.getUserByUsername(username))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                UsernameNotFoundException.class,
                () -> this.appUserDetailsService.loadUserByUsername(username)
        );
    }

}