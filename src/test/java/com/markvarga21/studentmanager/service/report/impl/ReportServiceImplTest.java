package com.markvarga21.studentmanager.service.report.impl;

import com.markvarga21.studentmanager.dto.ReportMessage;
import com.markvarga21.studentmanager.entity.Report;
import com.markvarga21.studentmanager.exception.ReportNotFoundException;
import com.markvarga21.studentmanager.repository.ReportRepository;
import com.markvarga21.studentmanager.service.mail.MailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {
    /**
     * The service which handles the reports.
     */
    @InjectMocks
    private ReportServiceImpl reportService;

    /**
     * The repository for the reports.
     */
    @Spy
    private ReportRepository repository;

    /**
     * The mail service used to send emails.
     */
    @Spy
    private MailService mailService;

    /**
     * A sample report message.
     */
    static final ReportMessage REPORT_MESSAGE = new ReportMessage(
            "JohnDoe",
            "Test subject",
            "Test description"
    );

    /**
     * A sample report.
     */
    static final Report REPORT = Report.builder()
            .issuerUsername(REPORT_MESSAGE.getUsername())
            .subject(REPORT_MESSAGE.getSubject())
            .description(REPORT_MESSAGE.getDescription())
            .build();

    @Test
    void shouldFetchAllReportsTest() {
        // Given
        List<Report> expected = List.of(REPORT);

        // When
        when(this.repository.findAll())
                .thenReturn(expected);
        List<Report> actual = this.reportService.getAllReports();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void shouldSendReportTest() throws MessagingException {
        // Given
        String expected = "Report sent successfully.";

        // When
        String actual = this.reportService
                .sendReport(REPORT_MESSAGE);

        // Then
        verify(this.repository, times(1))
                .save(REPORT);
        verify(this.mailService, times(1))
                .sendMail(REPORT);
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionUponSendingReportTest() throws MessagingException {
        // Given
        ReportMessage reportMessage = new ReportMessage(
                "JohnDoe",
                "Test subject",
                "Test description"
        );
        Report report = Report.builder()
                .issuerUsername(reportMessage.getUsername())
                .subject(reportMessage.getSubject())
                .description(reportMessage.getDescription())
                .build();
        String expected = "An error occurred while sending the report.";

        // When
        when(this.mailService.sendMail(report))
                .thenThrow(MessagingException.class);
        String actual = this.reportService.sendReport(reportMessage);

        // Then
        verify(this.repository, times(1))
                .save(report);
        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteReportIfExistsTest() {
        // Given
        Long reportId = 1L;

        // When
        when(this.repository.findById(reportId))
                .thenReturn(java.util.Optional.of(REPORT));
        this.reportService.deleteReport(reportId);

        // Then
        verify(this.repository, times(1))
                .deleteById(reportId);
    }

    @Test
    void shouldThrowExceptionUponDeletingReportIfNotExistsTest() {
        // Given
        Long reportId = 1L;

        // When
        when(this.repository.findById(reportId))
                .thenReturn(java.util.Optional.empty());

        // Then
        assertThrows(ReportNotFoundException.class, () -> {
            this.reportService.deleteReport(reportId);
        });
    }

}