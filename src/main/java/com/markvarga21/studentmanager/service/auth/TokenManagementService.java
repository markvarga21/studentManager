package com.markvarga21.studentmanager.service.auth;

import org.springframework.stereotype.Service;

/**
 * A service for managing token related operations like
 * invalidating/blacklisting tokens- or verifying their availability.
 */
@Service
public interface TokenManagementService {
    /**
     * Blacklist a token.
     *
     * @param token The token to blacklist.
     */
    void blacklistToken(String token);

    /**
     * Checks if a token is blacklisted or not.
     *
     * @param token The token to check.
     * @return {@code true} if the token is blacklisted, {@code false} otherwise.
     */
    boolean isBlacklisted(String token);

    /**
     * Adds a token to the database.
     *
     * @param token The token to be added.
     */
    void addToken(String token);

    /**
     * Deletes expired tokens from the database.
     */
    void deleteExpiredTokens();
}
