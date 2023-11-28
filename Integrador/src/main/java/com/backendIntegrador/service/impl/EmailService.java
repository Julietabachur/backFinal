package com.backendIntegrador.service.impl;
import com.backendIntegrador.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private final JavaMailSender mailSender;

    public EmailService (JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String message) throws MailException {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("Riskko <maildvrsend@gmail.com>");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true); // El segundo parámetro indica que el texto es HTML

            mailSender.send(mimeMessage);

        } catch (MailException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}

