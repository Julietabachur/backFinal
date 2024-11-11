package com.backendIntegrador.service.impl;

import com.backendIntegrador.service.IEmailService;
import com.resend.*;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.springframework.stereotype.Service;

@Service
public class EmailResend implements IEmailService {

    private String RESEND_API_KEY = "re_ETFujA7k_8HRf8M7CsuQX8mhxLcTJCj2L";
    private Resend resend;

    public EmailResend() {
        resend = new Resend(RESEND_API_KEY);
    }

    public void sendEmail(String to, String subject, String message) {
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("Valkiria <v4lkiria.soporte@gmail.com>") // risko.com requiere dar de alta el dominio y verificar DNS
                .to(to)  // usar "to" sin "" para que el destinatario use el pasado como argumento igual el subject
                .subject(subject)
                .html("<strong>Bienvenido a Valkiria!</strong><br>Gracias por registrarte en nuestra web.<br><br>" + message) // Concatenamos el mensaje con el HTML
                .build();

        try {
            SendEmailResponse data = resend.emails().send(sendEmailRequest);
            System.out.println("Email enviado!");
            System.out.println(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }
}