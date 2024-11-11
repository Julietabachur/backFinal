package com.backendIntegrador.service.impl;

import com.backendIntegrador.service.IEmailService;
import com.resend.*;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.springframework.stereotype.Service;

@Service
public class EmailResend implements IEmailService {

    private String RESEND_API_KEY = "re_FkvzR7pK_BPNpS3Pf5DDad81ZhBkS1tGp";
    private Resend resend;

    public EmailResend() {
        resend = new Resend(RESEND_API_KEY);
    }

    public void sendEmail(String to, String subject, String message) {
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("RISKKO <info@resend.dev>") // risko.com requiere dar de alta el dominio y verificar DNS
                .to("pacho.baires@gmail.com")  // usar "to" sin "" para que el destinatario use el pasado como argumento igual el subject
                .subject(subject)
                .html("<strong>Bienvenido a Riskko!</strong><br>Gracias por registrarte en nuestra web.<br><br>" + message) // Concatenamos el mensaje con el HTML
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
