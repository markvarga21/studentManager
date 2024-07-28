package com.markvarga21.studentmanager.service.auth.impl;

import com.markvarga21.studentmanager.entity.TokenInfo;
import com.markvarga21.studentmanager.exception.TokenNotFoundException;
import com.markvarga21.studentmanager.repository.TokenInfoRepository;
import com.markvarga21.studentmanager.service.auth.TokenManagementService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service for managing tokens.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Getter
public class TokenManagementServiceImpl implements TokenManagementService {
    /**
     * The token database.
     */
    private final TokenInfoRepository tokenInfoRepository;

    /**
     * The expiration time for the JWT token
     * measured in milliseconds.
     */
    @Value("${jwt.expiration.time.minutes}")
    private Long expirationTimeInMinutes;

    /**
     * Blacklist a token for revoking further
     * usages of it.
     *
     * @param token The token to blacklist.
     */
    @Override
    public void blacklistToken(final String token) {
        Optional<TokenInfo> tokenInfo = this.tokenInfoRepository
                .getTokenInfoByToken(token);
        if (tokenInfo.isPresent() && tokenInfo.get().isBlacklisted()) {
            String message = "Token already blacklisted.";
            log.error(message);
            throw new TokenNotFoundException(message);
        }
        TokenInfo tokenToUpdate = tokenInfo.get();
        tokenToUpdate.setBlacklisted(true);
        this.tokenInfoRepository.save(tokenToUpdate);
        log.info("Token blacklisted.");
    }

    /**
     * Checks if a token is blacklisted or not.
     *
     * @param token The token to check.
     * @return {@code true} if the token is blacklisted, {@code false} otherwise.
     */
    @Override
    public boolean isBlacklisted(final String token) {
        Optional<TokenInfo> tokenInfo = this.tokenInfoRepository
                .getTokenInfoByToken(token);
        return tokenInfo.get().isBlacklisted();
    }

    /**
     * Adds a token to the database.
     *
     * @param token The token to add.
     */
    @Override
    public void addToken(final String token) {
        TokenInfo tokenInfo = TokenInfo.builder()
                .token(token)
                .blacklisted(false)
                .issueDate(LocalDateTime.now())
                .build();
        this.tokenInfoRepository.save(tokenInfo);
        log.info("Token saved in the database.");
    }

    /**
     * Deletes expired tokens from the database.
     */
    @Override
    public void deleteExpiredTokens() {
        this.tokenInfoRepository
                .findAll()
                .stream()
                .filter(tokenInfo -> tokenInfo
                        .getIssueDate()
                        .isBefore(
                            LocalDateTime.now()
                                .minusMinutes(this.getExpirationTimeInMinutes())
                        ))
                .forEach(this.tokenInfoRepository::delete);
    }
}
