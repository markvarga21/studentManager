package com.markvarga21.studentmanager.service.mail.impl;

import com.markvarga21.studentmanager.entity.Report;
import com.markvarga21.studentmanager.service.mail.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * A service used for sending emails.
 */
@Service
@RequiredArgsConstructor
@Getter
public class MailServiceImpl implements MailService {
    /**
     * The JavaMailSender object used to send emails.
     */
    private final JavaMailSender mailSender;

    /**
     * The email address to send the email to.
     */
    @Value("${mail.to}")
    private String mailTo;

    /**
     * The email address to send the email from.
     */
    @Value("${mail.from}")
    private String mailFrom;

    /**
     * The HTML body of the email.
     */
    public static final String HTML_BODY = """
        <!DOCTYPE html>
        <html>
          <body style="font-family: 'Tahoma'">
            <table style="width: 30svw">
              <tr>
                <span style="font-weight: 800; font-size: xx-large">%s</span>
              </tr>
              <tr>
                <td style="color: rgb(139, 139, 139); font-weight: 200">@%s • %s</td>
              </tr>
              <tr>
                <td style="padding-top: 1rem">"%s"</td>
              </tr>
            </table>
          </body>
        </html>
        """;

    /**
     * The date and time formatter.
     */
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * The method responsible for sending an email.
     *
     * @param report The report to send.
     * @return An informational/status message.
     */
    @Override
    public String sendMail(final Report report) throws MessagingException {
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        String formattedTimeStamp = DATE_TIME_FORMATTER.format(report.getTimestamp());
        String filledHtmlContent = String.format(
                HTML_BODY,
                report.getSubject(),
                report.getIssuerUsername(),
                formattedTimeStamp,
                report.getDescription()
        );
        helper.setText(filledHtmlContent, true);
        helper.setTo(this.mailTo);
        helper.setSubject(report.getSubject());
        helper.setFrom(this.mailFrom);
        this.mailSender.send(mimeMessage);
        return "The report has been sent to the email address.";
    }
}
