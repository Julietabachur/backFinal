package com.backendIntegrador.controller;

import com.backendIntegrador.model.Client;
import com.backendIntegrador.service.impl.ClientService;
import com.backendIntegrador.service.impl.EmailService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("api/v1/private/email")
@RequiredArgsConstructor

public class EmailController {

    @Autowired
    private final EmailService emailService;
    private final ClientService clientService;


    @PostMapping("/")   // Recibe id en el body y reenvia el mail de confirmación.
    public ResponseEntity<?> update(@RequestBody String datosMailer) {

        String id;
        String front_url;
        String verify_url;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Analizar la cadena JSON en un objeto Java
            Map<String, Object> jsonMap = objectMapper.readValue(datosMailer, new TypeReference<Map<String, Object>>() {
            });

            // Accede a los elementos del objeto JSON según sea necesario
            id = (String) jsonMap.get("id");
            front_url = (String) jsonMap.get("front_url");
            verify_url = (String) jsonMap.get("verify_url");

            System.out.println("DATOS RECIBIDOS");
            System.out.println(id);
            System.out.println(front_url);
            System.out.println("********************");

            try {
                // Verifica si el usuario con el ID existe
                Client existingUser = clientService.getClientById(id);
                if (existingUser == null) {
                    // Usuario no encontrado, devuelve un error 404
                    return ResponseEntity.notFound().build();
                }

                // Llama al servicio de envio de mails para reenviarlo
                try {
                    reSendNotificationEmail(existingUser, front_url, verify_url);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el envío de mail.");
                }
                return ResponseEntity.ok().body(existingUser.getEmail());

            } catch (Exception e) {
                // Maneja cualquier excepción que pueda ocurrir
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error usuario inexistente.");
            }

        } catch (IOException e) {
            // Maneja cualquier excepción que pueda ocurrir al analizar el JSON
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al analizar la solicitud HTTP.");
        }


    }


    private void reSendNotificationEmail(Client existingUser, String front_url, String verify_url) {

        String login_url = front_url + "/login";

        // Prepara el mensaje y el asunto
        String subject = "Bienvenido a Riskko";

        // Cuerpo del mensaje en formato HTML
        String htmlMessage = "<html><body>" +
                "<p>Hola " + existingUser.getFirstName() + ",</p>" +
                "<p>Sus datos de registro:</p>" +
                "<p>Nombre de usuario: " + existingUser.getClientName() + "</p>" +
                "<p>E-mail: " + existingUser.getEmail() + "</p>" +
                "<p>Para ingresar al sitio, visite: <a href='" + login_url + "'>" + login_url + "</a></p>" +
                "<p>Para verificar su mail: <a href='" +front_url + verify_url + "'> Haga Click Aquí </a></p>" +
                "</body></html>";

        // Envía el correo
        emailService.sendEmail(existingUser.getEmail(), subject, htmlMessage);

    }
}
