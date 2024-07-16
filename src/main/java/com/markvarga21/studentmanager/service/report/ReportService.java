package com.markvarga21.studentmanager.service.report;

import com.markvarga21.studentmanager.dto.ReportMessage;
import com.markvarga21.studentmanager.entity.Report;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A service which is used to access report data.
 */
@Service
public interface ReportService {
    /**
     * Sends a report to the system.
     *
     * @param reportMessage The report message object.
     * @return An informational/status message.
     */
    String sendReport(ReportMessage reportMessage);

    /**
     * Retrieves all the reports.
     *
     * @param id The ID of the report.
     */
    void deleteReport(Long id);

    /**
     * Retrieves all the reports.
     *
     * @return A list of all the reports.
     */
    List<Report> getAllReports();
}
