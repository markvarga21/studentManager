package com.markvarga21.studentmanager.service.report;

import com.markvarga21.studentmanager.dto.ReportMessage;
import com.markvarga21.studentmanager.entity.Report;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * A service which is used to operate on reports.
 */
@Service
public interface ReportService {
    /**
     * Sends a report to the system.
     *
     * @param reportMessage The report message object.
     * @return An informational message.
     */
    String sendReport(ReportMessage reportMessage) throws MessagingException;

    /**
     * Retrieves all reports.
     *
     * @param id The ID of the report.
     */
    void deleteReport(Long id);

    /**
     * Retrieves all reports.
     *
     * @param page The page number.
     * @param size The number of reports on a single page.
     * @return A page containing a subset of reports.
     */
    Page<Report> getAllReports(Integer page, Integer size);
}
