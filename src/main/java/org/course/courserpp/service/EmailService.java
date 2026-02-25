package org.course.courserpp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPdfReport(String toEmail, byte[] pdfData, String filename) throws MessagingException {
        // Validate email format before sending
        try {
            new InternetAddress(toEmail).validate();
        } catch (Exception e) {
            throw new IllegalArgumentException("Некорректный адрес электронной почты: " + toEmail);
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Отчёт по счетам — CourseRPP");
        helper.setText("Добрый день!\n\nВо вложении находится PDF-отчёт по выбранным счетам.\n\nС уважением,\nCourseRPP", false);
        helper.addAttachment(filename, new ByteArrayResource(pdfData), "application/pdf");

        mailSender.send(message);
        log.info("PDF-отчёт успешно отправлен на адрес: {}", toEmail);
    }
}
