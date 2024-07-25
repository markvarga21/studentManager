package com.markvarga21.studentmanager.util;

import com.markvarga21.studentmanager.service.auth.TokenManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * The TokenDeleter class is used to periodically
 * delete expired tokens.
 */
@Component
@RequiredArgsConstructor
public class TokenDeleter {
    /**
     * The token database.
     */
    private final TokenManagementService tokenManagementService;

    /**
     * Deletes expired tokens every day at midnight.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredTokens() {
        this.tokenManagementService.deleteExpiredTokens();
    }
}
