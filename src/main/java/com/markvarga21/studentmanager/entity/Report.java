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
     * This method is used to compare two reports
     * if they are the same or not taking into account
     * the issue date of the report.
     *
     * @param o The object to compare with.
     * @return True if the reports are the same, false otherwise.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Report report)) {
            return false;
        }
        return this.issuerUsername.equals(report.issuerUsername)
                && this.subject.equals(report.subject)
                && this.description.equals(report.description);
    }

    /**
     * This method is used to generate a hash code for the report.
     *
     * @return The hash code of the report.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
