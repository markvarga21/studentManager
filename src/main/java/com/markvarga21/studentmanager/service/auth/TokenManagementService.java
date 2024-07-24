package com.markvarga21.studentmanager.service.auth;

import org.springframework.stereotype.Service;

/**
 * Service for managing token blacklisting.
 */
@Service
public interface TokenManagementService {
    /**
     * Blacklist a token.
     *
     * @param token the token to blacklist
     */
    void blacklistToken(String token);

    /**
     * Checks if a token is blacklisted.
     *
     * @param token the token to check
     * @return true if the token is blacklisted, false otherwise
     */
    boolean isBlacklisted(String token);

    /**
     * Add a token to the database.
     *
     * @param token the token to add.
     */
    void addToken(String token);

    /**
     * Delete expired tokens from the database.
     */
    void deleteExpiredTokens();
}
