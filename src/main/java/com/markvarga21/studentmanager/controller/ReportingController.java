package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.dto.ReportMessage;
import com.markvarga21.studentmanager.entity.Report;
import com.markvarga21.studentmanager.service.report.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A controller which is used to handle user reports/feedback.
 */
@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@Tag(name = "Reporting services", description = "The reporting related endpoints.")
public class ReportingController {
    /**
     * The service which handles the reports.
     */
    private final ReportService reportService;

    /**
     * Retrieves all the reports.
     *
     * @return A list of all the reports.
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Report> getReports() {
        return this.reportService.getAllReports();
    }

    /**
     * Deletes a report from the database.
     *
     * @param id The ID of the report.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteReport(@PathVariable final Long id) {
        this.reportService.deleteReport(id);
        String message = String.format(
                "The report with the id '%d' was deleted.",
                id
        );
        return ResponseEntity.ok(message);
    }

    /**
     * Sends a report to the system.
     *
     * @param reportMessage The report message object.
     * @return An informational/status message.
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> sendReport(
            @RequestBody @Validated final ReportMessage reportMessage
    ) {
        String status = this.reportService
                .sendReport(reportMessage);
        return ResponseEntity.ok(status);
    }
}
