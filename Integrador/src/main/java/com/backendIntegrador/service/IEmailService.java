package com.backendIntegrador.service;

public interface IEmailService {
    void sendEmail(String to, String subject, String message);
}
