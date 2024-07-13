package com.markvarga21.studentmanager.util;

import com.markvarga21.studentmanager.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminUserInitTest {
    /**
     * The admin user init utility component under testing.
     */
    @InjectMocks
    private AdminUserInit adminUserInit;

    /**
     * The app user repository for mocking the repository.
     */
    @Spy
    private AppUserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldInitAdminUser() {
        // Given
        String mockEncryptedAdminPassword = "mockEncryptedAdminPassword";

        // When
        when(this.passwordEncoder.encode(any()))
                .thenReturn(mockEncryptedAdminPassword);
        when(this.repository.findByUsername("admin"))
                .thenReturn(Optional.ofNullable(null));

        // Ensure admin user initialization method is called
        this.adminUserInit.onApplicationEvent(null);

        // Then
        assertNotNull(this.repository.findByUsername("admin"));
    }

}