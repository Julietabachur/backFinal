package com.backendIntegrador.service;

import org.springframework.mail.MailException;

public interface IEmailService {
    void sendEmail(String to, String subject, String message) throws MailException;
}
