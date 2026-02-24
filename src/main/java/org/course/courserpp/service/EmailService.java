package org.course.courserpp.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPdfReport(String to, byte[] pdfBytes, String filename) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Отчёт по счетам — CourseRPP");
            helper.setText("Здравствуйте!\n\nВо вложении находится PDF-отчёт по выбранным счетам.\n\nС уважением,\nCourseRPP");
            helper.addAttachment(filename, () -> new java.io.ByteArrayInputStream(pdfBytes), "application/pdf");
            mailSender.send(message);
            log.info("PDF report sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send PDF report to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Не удалось отправить письмо на адрес " + to + ": " + e.getMessage(), e);
        }
    }
}
