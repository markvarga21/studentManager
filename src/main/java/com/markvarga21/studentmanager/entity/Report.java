package com.markvarga21.studentmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A report entity which is used to store reports.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    /**
     * The id of the report.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The issuer of the report.
     */
    private String issuerUsername;

    /**
     * The subject of the report.
     */
    private String subject;

    /**
     * The description of the report.
     */
    @Column(nullable = false, length = MAX_DESCRIPTION_LENGTH_IN_CHARACTERS)
    private String description;

    /**
     * The timestamp of the report.
     */
    @Column(nullable = false)
    private LocalDateTime timestamp;

    /**
     * The maximum length of the description.
     */
    static final int MAX_DESCRIPTION_LENGTH_IN_CHARACTERS = 1000;

    /**
     * Util method for automatically generating the timestamp
     * for the report.
     */
    @PrePersist
    protected void prePersist() {
        this.timestamp = LocalDateTime.now();
    }
}
