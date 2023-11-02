package com.backendIntegrador.controller;


import com.backendIntegrador.model.Client;
import com.backendIntegrador.service.impl.ClientService;
import com.backendIntegrador.service.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/private/email")
@RequiredArgsConstructor

public class EmailController {

    @Autowired
    private final EmailService emailService;
    private final ClientService clientService;


    @PostMapping("/server")   // Recibe id en el body y reenvia el mail de confirmación.
    public ResponseEntity<?> update(@RequestBody Client client,@RequestParam String server) {

        try {
            // Verifica si el usuario con el ID existe
            Client existingUser = clientService.getClientById(client.getId());
            if (existingUser == null) {
                // Usuario no encontrado, devuelve un error 404
                return ResponseEntity.notFound().build();
            }
            // Llama al servicio de envio de mails para reenviarlo
            try{
                reSendNotificationEmail(existingUser, server);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            // Maneja cualquier excepción que pueda ocurrir
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la actualización");
        }
    }




    private void reSendNotificationEmail(Client existingUser, String server_url) {

        // Prepara el mensaje y el asunto
        String subject = "Bienvenido a Riskko";
        String message = "Hola " + existingUser.getFirstName() + ",\n\n" +
                "Sus datos de registro:\n" +
                "Nombre de usuario: " + existingUser.getClientName() + "\n" +
                "E-mail: " + existingUser.getEmail() + "\n" +
                "Otro contenido del mensaje...\n\n" +
                "Para ingresar al sitio, visite: " + server_url + "\n";

        // Envía el correo
        emailService.sendEmail(existingUser.getEmail(), subject, message);
        //emailResend.sendEmail(userEmail, subject, message);
    }
}
