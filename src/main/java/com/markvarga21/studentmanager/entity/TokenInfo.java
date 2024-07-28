package com.markvarga21.studentmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a token in the application with some
 * useful metainformation.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {
    /**
     * The unique identifier of the token.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The maximum length of the token.
     */
    private static final int TOKEN_LENGTH = 512;

    /**
     * The token string.
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    @Column(length = TOKEN_LENGTH)
    private String token;

    /**
     * Whether the token is blacklisted or not.
     */
    private boolean blacklisted;

    /**
     * The issue date of the token.
     */
    private LocalDateTime issueDate;
}
