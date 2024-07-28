package com.markvarga21.studentmanager.service.mail;

import com.markvarga21.studentmanager.entity.Report;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

/**
 * A service used for sending emails.
 */
@Service
public interface MailService {
    /**
     * Sends an email to the specified address.
     *
     * @param report The report to send.
     * @return An informational message.
     */
    String sendMail(Report report) throws MessagingException;
}
