package com.backendIntegrador.controller;


import com.backendIntegrador.DTO.ClientDto;
import com.backendIntegrador.model.Client;
import com.backendIntegrador.service.impl.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    @Autowired
    private final ClientService clientService;

    @PostMapping("")
    public ResponseEntity<?> save( @RequestBody Client client ) {
        try {
            return ResponseEntity.ok().body(clientService.save(client));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. En save\"}");
        }

    }

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        try {
            List<Client> clientList = clientService.clientList();
            List<ClientDto> clientDtoList = new ArrayList<>();

            ClientDto clientDto = new ClientDto();
            for (Client client : clientList) {
                clientDto.setClientName(client.getClientName());
                clientDto.setId(client.getId());
                clientDto.setEmail(client.getEmail());
                clientDto.setCel(client.getCel());
            }

            clientDtoList.add(clientDto);
            return ResponseEntity.ok().body(clientDtoList);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error. en Findall");
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById( @PathVariable("id") String id ){
        try{
            Client client = clientService.getClientById(id);
            ClientDto clientDto = new ClientDto();
                clientDto.setClientName(client.getClientName());
                clientDto.setId(client.getId());
                clientDto.setEmail(client.getEmail());
                clientDto.setCel(client.getCel());

            return ResponseEntity.ok().body(clientDto);
        }catch (Exception e){
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. En getClientById\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) throws Exception {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
