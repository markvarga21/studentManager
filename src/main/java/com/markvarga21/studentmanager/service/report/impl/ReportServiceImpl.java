package com.markvarga21.studentmanager.service.report.impl;

import com.markvarga21.studentmanager.dto.ReportMessage;
import com.markvarga21.studentmanager.entity.Report;
import com.markvarga21.studentmanager.exception.ReportNotFoundException;
import com.markvarga21.studentmanager.repository.ReportRepository;
import com.markvarga21.studentmanager.service.mail.MailService;
import com.markvarga21.studentmanager.service.report.ReportService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
     * The mail service used to send emails.
     */
    private final MailService mailService;

    /**
     * Retrieves all reports.
     *
     * @param page The actual page.
     * @param size The number of reports in a single page.
     * @return A page containing a subset of reports.
     */
    @Override
    public Page<Report> getAllReports(
            final Integer page,
            final Integer size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return this.repository.findAll(pageRequest);
    }

    /**
     * Sends a report to the system.
     *
     * @param reportMessage The report message object.
     * @return An informational message.
     */

    @Override
    public String sendReport(final ReportMessage reportMessage)  {
        try {
            Report report = Report.builder()
                    .issuerUsername(reportMessage.getUsername())
                    .subject(reportMessage.getSubject())
                    .description(reportMessage.getDescription())
                    .build();
            this.repository
                    .save(report);
            String emailStatusMessage = this.mailService
                    .sendMail(report);
            log.info(emailStatusMessage);
            return "Report sent successfully.";
        } catch (MessagingException e) {
            log.error("An error occurred while sending the report.");
            return "An error occurred while sending the report.";
        }
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
}
