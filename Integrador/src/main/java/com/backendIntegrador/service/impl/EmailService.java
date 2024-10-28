package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Client;
import com.backendIntegrador.model.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String from, String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendWithImageFromURLLogin(String from, String to, String subject, Optional<Client> client) throws MessagingException {
        String htmlBody = "<html><body>" +
                "<p>Hola " + client.get().getClientName() + "!</p>" +
                "<p>Te notificamos que el inicio de sesión se ha realizado de manera exitosa.</p>" +
                "<p>Si tienes alguna pregunta o necesitas asistencia, no dudes en contactarnos.</p>" +
                "<p>Saludos,<br>Valkiria</p>" +
                "<br>" +
                "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/LogoSinfoniaEmail.png' alt='Logo'>" +
                "<br>" +
                "<b>Valkiria - Equipo de Soporte Técnico" +
                "<br>" +
                "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/separador.png' alt='Separador'>" +
                "<br>" +
                "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/face-b.png' alt='ico-Facebook' style='margin-right: 15px;'>" +
                "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/instagram-b.png' alt='ico-Instagram' style='margin-right: 15px;'>" +
                "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/twitter-b.png' alt='ico-Twitter' style='margin-right: 15px;'>" +
                "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/youtube-b.png' alt='ico-Youtube' style='margin-right: 15px;'>" +
                "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/mapa-b.png' alt='ico-mapa'>" +
                "</body></html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }

    public void sendWithImageFromURLRegister(String from, String to, String subject, Client client) throws MessagingException {
        String htmlBody = "<html><body>" +
                "<p>Hola " + client.getClientName() + "!</p>" +
                "<p>Es un placer darte la bienvenida a Valkiria, nos complace confirmar que tu registro ha sido exitoso y que ahora eres parte de nuestra comunidad.</p>" +
                "<p>A continuación, te proporcionamos los detalles de tu registro: <br>" +
                "\n" +
                "<b>• Nombre:</b> " + client.getFirstName() + " " + client.getLastName() + "<br>" +
                "<b>• Email:</b> " + client.getEmail() + "</p>" +
                "<p>Para confirmar que la información ingresada es correcta, por favor, haz clic en el siguiente enlace para iniciar sesión en tu cuenta recién creada:\n" +
                "\n" +
                "http://localhost:5173/login</p>" +
                "<p>Si tienes alguna pregunta o necesitas asistencia, no dudes en contactarnos.</p>" +
                "<p>Saludos,<br>Valkiria</p>" +
                "<br>" +
                "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/LogoSinfoniaEmail.png' alt='Logo'>" +
                "<br>" +
                "<b>Valkiria - Equipo de Soporte Técnico" +
                "<br>" +
                "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/separador.png' alt='Separador'>" +
                "<br>" +
                "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/face-b.png' alt='ico-Facebook' style='margin-right: 15px;'>" +
                "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/instagram-b.png' alt='ico-Instagram' style='margin-right: 15px;'>" +
                "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/twitter-b.png' alt='ico-Twitter' style='margin-right: 15px;'>" +
                "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/youtube-b.png' alt='ico-Youtube' style='margin-right: 15px;'>" +
                "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/mapa-b.png' alt='ico-mapa'>" +
                "</body></html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }



    public void sendWithImageFromURLSell(String from, String to, String subject, Client client, Set<Product> products) throws MessagingException {

        StringBuilder productsSelled = new StringBuilder();

        for (Product product : products) {
            productsSelled.append("- ").append(product.getProductName()).append("<br>");
        }


            String htmlBody = "<html><body>" +
                    "<p>Hola " + client.getClientName() + "!</p>" +
                    "<p>¡Gracias por elegir a <b>Sinfonía</b> para reservar tus instrumentos musicales! Estamos encantados de confirmar que hemos recibido tu solicitud de reserva y estamos procesándola con éxito. A continuación, te proporcionamos los detalles de tu reserva: </p>" +

//                    "<b>• Número de reserva:</b> " + reserva.getId() + "<br>" +
//                    "<b>• Fecha de reserva:</b> " + reserva.getFechaReserva() + "<br>" +
//                    "<b>• Fecha inicio de reserva:</b> " + reserva.getFechaInicio() + "<br>" +
//                    "<b>• Fecha fin de reserva:</b> " + reserva.getFechaFin() + "<br>" +
//                    "<b>• Productos reservados:</b> <br>" + productosReservados.toString() + "</p>" +


                    "<p>Apreciamos tu confianza en <b>Valkiria</b> y esperamos que disfrutes de tus instrumentos reservados. ¡Esperamos verte pronto en nuestra tienda http://localhost:5173/login! </p>" +

                    "<p>Si tienes alguna pregunta o necesitas asistencia, no dudes en contactarnos.</p>" +
                    "<p>Saludos,<br>Valkiria</p>" +

                    "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/LogoSinfoniaEmail.png' alt='Logo'>" +
                    "<br>" +
                    "<b>Valkiria - Equipo de Soporte Técnico" +
                    "<br>" +
                    "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/separador.png' alt='Separador'>" +
                    "<br>" +
                    "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/face-b.png' alt='ico-Facebook' style='margin-right: 15px;'>" +
                    "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/instagram-b.png' alt='ico-Instagram' style='margin-right: 15px;'>" +
                    "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/twitter-b.png' alt='ico-Twitter' style='margin-right: 15px;'>" +
                    "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/youtube-b.png' alt='ico-Youtube' style='margin-right: 15px;'>" +
                    "<img src='https://img-c9-g2-bucket.s3.amazonaws.com/Email/mapa-b.png' alt='ico-mapa'>" +
                    "</body></html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }
}

