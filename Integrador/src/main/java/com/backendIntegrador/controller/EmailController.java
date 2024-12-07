    package com.backendIntegrador.controller;

    import com.backendIntegrador.DTO.ProductDto;
    import com.backendIntegrador.model.Car;
    import com.backendIntegrador.model.Client;
    import com.backendIntegrador.model.Sale;
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
                front_url = front_url.substring(0, front_url.length() - 1);
                verify_url = (String) jsonMap.get("verify_url");


                System.out.println("DATOS EMAIL RECIBIDOS");


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

            String login_url = front_url;

            // Prepara el mensaje y el asunto
            String subject = "Bienvenido a Valkiria,";

            // Cuerpo del mensaje en formato HTML
//            String htmlMessage = "<html><body>" +
//                    "<p>Hola " + existingUser.getFirstName() + ",</p>" +
//                    "<p>Sus datos de registro:</p>" +
//                    "<p>Nombre de usuario: " + existingUser.getClientName() + "</p>" +
//                    "<p>E-mail: " + existingUser.getEmail() + "</p>" +
//                    "<p>Para ingresar al sitio, visite: <a href=' nuestro website.'>" + login_url + "</a></p>" +
//                    "<p>Para verificar su mail: <a href='" +front_url + verify_url + "'> Haga Click Aquí </a></p>" +
//                    "</body></html>";

            // Cuerpo del mensaje en formato HTML con estilo mejorado
            String htmlMessage = "<html><body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>"
                    + "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); padding: 20px;'>"
                    + "<h2 style='color: #333;'>¡Bienvenido a Valkiria, " + existingUser.getFirstName() + "!</h2>"
                    + "<p style='color: #555;'>Gracias por unirse a nuestra plataforma. A continuación, encontrará sus datos de registro:</p>"
                    + "<ul style='color: #555; line-height: 1.6;'>"
                    + "<li><strong>Nombre de usuario:</strong> " + existingUser.getClientName() + "</li>"
                    + "<li><strong>E-mail:</strong> " + existingUser.getEmail() + "</li>"
                    + "</ul>"
                    + "<p style='color: #555;'>Por favor, verifique su email haciendo click "
                    + "<a href='" + front_url + verify_url + "' style='color: #1a73e8;'>aquí</a>.</p>"
                    + "<p style='color: #888; font-size: 12px; text-align: center;'>Si tiene alguna pregunta, no dude en ponerse en contacto con nuestro equipo de soporte.</p>"
                    + "</div></body></html>";

            // Envía el correo - prueba cambio
            emailService.sendEmail(existingUser.getEmail(), subject, htmlMessage);

        }

        @PostMapping("/car")
        public void sendNotificationSale(@RequestBody Sale sale) throws Exception {
            Client existingUser = clientService.getClientById(sale.getIdUser());

            // Construye la lista de productos en formato HTML
            StringBuilder productsHtml = new StringBuilder();
            for (ProductDto product : sale.getProductList()) {
                productsHtml.append("<li>")
                        .append("<strong>Producto:</strong> ").append(product.getProductName()).append("<br>")
                        .append("<strong>Talle:</strong> ").append(product.getSize()).append("<br>")
                        .append("<strong>Precio:</strong> $").append(product.getPrice()).append("<br>")
                        .append("</li>");
            }

            // Prepara el mensaje y el asunto
            String subject = "Gracias por su compra,";

            String htmlMessage = "<html><body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>"
                    + "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); padding: 20px;'>"
                    + "<h2 style='color: #333;'>¡Buenas, " + existingUser.getFirstName() + " " + existingUser.getLastName() + "!</h2>"
                    + "<p style='color: #555;'>Gracias por su compra. A continuación, encontrará el detalle de la misma:</p>"
                    + "<ul style='color: #555; line-height: 1.6;'>"
                    + "<li><strong>Nombre de usuario:</strong> " + existingUser.getClientName() + "</li>"
                    + "<li><strong>E-mail:</strong> " + existingUser.getEmail() + "</li>"
                    + "</ul>"
                    + "</ul>"
                    + "<p style='color: #555;'>Productos comprados:</p>"
                    + "<ul style='color: #555; line-height: 1.6;'>"
                    + productsHtml
                    +
                    "<li> Price: " + sale.getTotalPrice()  + "</li>"
                    + "</ul>"
                    + "<p style='color: #888; font-size: 12px; text-align: center;'>Si tiene alguna pregunta, no dude en ponerse en contacto con nuestro equipo de soporte.</p>"
                    + "</div></body></html>";


            emailService.sendEmail(existingUser.getEmail(), subject, htmlMessage);

        }
    }