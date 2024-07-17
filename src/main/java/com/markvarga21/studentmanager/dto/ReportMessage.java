package com.markvarga21.studentmanager.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO class which is used to send a message
 * to the report service.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportMessage {
    /**
     * The username of the user who sent the report.
     */
    @NotEmpty(message = "The username must not be empty.")
    private String username;

    /**
     * The subject of the report.
     */
    @NotEmpty(message = "The subject must not be empty.")
    private String subject;

    /**
     * The description of the report.
     */
    @NotEmpty(message = "The description must not be empty.")
    private String description;
}
