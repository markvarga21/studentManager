package com.markvarga21.studentmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a token in the application.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {
    /**
     * The unique identifier for the token.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The token string.
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    @Column(length = 512)
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
