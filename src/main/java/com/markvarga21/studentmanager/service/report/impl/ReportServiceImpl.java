package com.markvarga21.studentmanager.service.report.impl;

import com.markvarga21.studentmanager.dto.ReportMessage;
import com.markvarga21.studentmanager.entity.Report;
import com.markvarga21.studentmanager.exception.ReportNotFoundException;
import com.markvarga21.studentmanager.repository.ReportRepository;
import com.markvarga21.studentmanager.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A service which handles error reporting.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    /**
     * The repository holding the reports.
     */
    private final ReportRepository repository;

    /**
     * Sends a report to the system.
     *
     * @param reportMessage The report message object.
     * @return An informational/status message.
     */
    @Override
    public String sendReport(final ReportMessage reportMessage) {
        Report report = Report.builder()
                .issuerUsername(reportMessage.getUsername())
                .subject(reportMessage.getSubject())
                .description(reportMessage.getDescription())
                .build();
        this.repository.save(report);
        return "Report sent successfully.";
    }

    /**
     * Deletes a report.
     *
     * @param id The ID of the report.
     */
    @Override
    public void deleteReport(final Long id) {
        Optional<Report> report = this.repository.findById(id);
        if (report.isEmpty()) {
            String message = String.format(
                    "The report with the ID %d was not found.",
                    id
            );
            log.error(message);
            throw new ReportNotFoundException(message);
        }
        this.repository.deleteById(id);
    }

    /**
     * Retrieves all the reports.
     *
     * @return A list of all the reports.
     */
    @Override
    public List<Report> getAllReports() {
        return this.repository.findAll();
    }
}
