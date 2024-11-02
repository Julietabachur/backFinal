package com.backendIntegrador.controller;

import com.backendIntegrador.model.Client;
import com.backendIntegrador.model.Reserve;
import com.backendIntegrador.service.impl.ClientService;
import com.backendIntegrador.service.impl.EmailService;
import com.backendIntegrador.service.impl.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/private/reserves")
@RequiredArgsConstructor
public class ReserveController {


    @Autowired
    private final ReserveService reserveService;
    @Autowired
    private final EmailService emailService;
    @Autowired
    private final ClientService clientService;

    @PostMapping("")
    public ResponseEntity<?> save( @RequestBody Reserve reserve ) {
        try {

            Reserve reserveCompleted = reserveService.save(reserve);
            if (reserveCompleted.getId() != null) {
                sendReserveEmail(reserve);
            }
            System.out.println("Mail de Reserva Enviado!");

            return ResponseEntity.ok().body(reserveCompleted);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        try {
            List<Reserve> reserveList = reserveService.reserveList();


            return ResponseEntity.ok().body(reserveList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error. en Findall");
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReserveById( @PathVariable("id") String id ) {
        try {
            Reserve reserve = reserveService.getReserveById(id);

            return ResponseEntity.ok().body(reserve);
        } catch (Exception e) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. En getReserveById\"}");
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete( @PathVariable("id") String id ) throws Exception {
        reserveService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void sendReserveEmail(Reserve reserve) throws Exception {

        String serverUrl = "http://riskko.s3-website-us-east-1.amazonaws.com/";
        String productDetailUrl = serverUrl + "detalle/";
        String clientId = reserve.getClientId();
        Client reserveUser = clientService.getClientById(clientId);

        // Prepara el mensaje y el asunto
        String subject = "Riskko - Reserva Confirmada";

        // Cuerpo del mensaje en formato HTML
        String htmlMessage = "<html><body>" +
                "<p>Hola " + reserveUser.getFirstName() + ",</p>" +
                "<p>Confirmamos tu reserva:</p>" +
                "<p>Producto: " + reserve.getProductName() + "</p>" +
                "<p><a href='" + productDetailUrl + reserve.getProductId() + "'>Clickea aquí</a> para ver el producto.</p>"+
                "<p><img width='400' height='400' src="+reserve.getReserveImg() +" alt="+reserve.getProductName()+"/></p>" +
                "<p>Fechas de tu reserva: "+ reserve.getStartDate()+ " - " + reserve.getEndDate()+" .</p>" +
                "<p>Muchas gracias!</p>" +
                "</body></html>";

        // Envía el correo
        // emailService.sendEmail(reserveUser.getEmail(), subject, htmlMessage);

    }

}
