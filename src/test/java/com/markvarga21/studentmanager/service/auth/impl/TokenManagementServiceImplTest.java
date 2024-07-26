package com.markvarga21.studentmanager.service.auth.impl;

import com.markvarga21.studentmanager.entity.TokenInfo;
import com.markvarga21.studentmanager.exception.TokenNotFoundException;
import com.markvarga21.studentmanager.repository.TokenInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenManagementServiceImplTest {
    /**
     * The service to be tested.
     */
    @InjectMocks
    private TokenManagementServiceImpl tokenManagementService;

    /**
     * The expiration time for the JWT token.
     */
    private static final Long EXPIRATION_TIME_IN_MINUTES = 30L;

    /**
     * The token database.
     */
    @Spy
    private TokenInfoRepository tokenInfoRepository;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        Field expirationTimeInMinutesField = TokenManagementServiceImpl.class
                .getDeclaredField("expirationTimeInMinutes");
        expirationTimeInMinutesField
                .setAccessible(true);
        expirationTimeInMinutesField
                .set(this.tokenManagementService, EXPIRATION_TIME_IN_MINUTES);
    }

    @Test
    void shouldBlacklistTokenIfNotBlacklistedTest() {
        // Given
        String token = "token";
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setBlacklisted(false);
        tokenInfo.setToken(token);

        // When
        when(this.tokenInfoRepository.getTokenInfoByToken(token))
                .thenReturn(Optional.of(tokenInfo));
        this.tokenManagementService.blacklistToken(token);

        // Then
        tokenInfo.setBlacklisted(true);
        verify(this.tokenInfoRepository).save(tokenInfo);
    }

    @Test
    void shouldThrowTokenNotFoundExceptionIfTokenAlreadyBlacklistedTest() {
        // Given
        String token = "token";
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setBlacklisted(true);
        tokenInfo.setToken(token);

        // When
        when(this.tokenInfoRepository.getTokenInfoByToken(token))
                .thenReturn(Optional.of(tokenInfo));

        // Then
        assertThrows(
                TokenNotFoundException.class,
                () -> this.tokenManagementService.blacklistToken(token)
        );
    }

    @Test
    void shouldReturnTrueIfTokenIsBlacklistedTest() {
        // Given
        String token = "token";
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setBlacklisted(true);

        // When
        when(this.tokenInfoRepository.getTokenInfoByToken(token))
                .thenReturn(Optional.of(tokenInfo));
        boolean actual = this.tokenManagementService.isBlacklisted(token);

        // Then
        assertTrue(actual);
    }

    @Test
    void shouldReturnFalseIfTokenIsBlacklistedTest() {
        // Given
        String token = "token";
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setBlacklisted(false);

        // When
        when(this.tokenInfoRepository.getTokenInfoByToken(token))
                .thenReturn(Optional.of(tokenInfo));
        boolean actual = this.tokenManagementService.isBlacklisted(token);

        // Then
        assertFalse(actual);
    }

    @Test
    void shouldAddTokenToDatabaseTest() {
        // Given
        String token = "token";

        // When
        this.tokenManagementService.addToken(token);

        // Then
        verify(this.tokenInfoRepository).save(any(TokenInfo.class));
    }

    @Test
    void shouldDeleteExpiredTokensTest() {
        // Given
        TokenInfo tokenInfo = TokenInfo.builder()
                .token("token")
                .issueDate(LocalDateTime.now().minusMinutes(EXPIRATION_TIME_IN_MINUTES + 1))
                .build();

        // When
        when(this.tokenInfoRepository.findAll())
                .thenReturn(List.of(tokenInfo));
        this.tokenManagementService.deleteExpiredTokens();

        // Then
        verify(this.tokenInfoRepository).findAll();
        verify(this.tokenInfoRepository).delete(tokenInfo);
    }
}