package com.backendIntegrador.controller;


import com.backendIntegrador.DTO.ClientDto;
import com.backendIntegrador.model.Client;
import com.backendIntegrador.model.Role;
import com.backendIntegrador.service.impl.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v1/private/clients")
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

    @GetMapping("/getMe")
    public ResponseEntity<?> getUserData() {
        try {


            // Get the authenticated user's details
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // You can access user details, such as username, email, etc., from userDetails
            String username = userDetails.getUsername();
            Client client = clientService.getClientByEmail(username);
            Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
            // You can also access other user-specific information depending on your application's UserDetailsService implementation.

            // Return the user data as a JSON response
            Map<String, Object> response = new HashMap<>();
            response.put("username", client.getClientName());
            response.put("roles", roles);

            // Add other user data as needed

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error. en getme");
        }
    }


    @GetMapping("")
    public ResponseEntity<?> findAll() {
        try {
            List<Client> clientList = clientService.clientList();
            List<ClientDto> clientDtoList = new ArrayList<>();

            for (Client client : clientList) {
                ClientDto clientDto = new ClientDto();
                clientDto.setFirstName(client.getFirstName());
                clientDto.setLastName(client.getLastName());
                clientDto.setClientName(client.getClientName());
                clientDto.setId(client.getId());
                clientDto.setEmail(client.getEmail());
                clientDto.setAddress(client.getAddress());
                clientDto.setRoles(client.getRoles());
                clientDto.setReserves(client.getReserves());
                clientDto.setCel(client.getCel());
                clientDtoList.add(clientDto);
            }


            return ResponseEntity.ok().body(clientDtoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error. en Findall");
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById( @PathVariable("id") String id ) {
        try {
            Client client = clientService.getClientById(id);
            ClientDto clientDto = new ClientDto();
            clientDto.setFirstName(client.getFirstName());
            clientDto.setLastName(client.getLastName());
            clientDto.setClientName(client.getClientName());
            clientDto.setId(client.getId());
            clientDto.setEmail(client.getEmail());
            clientDto.setAddress(client.getAddress());
            clientDto.setRoles(client.getRoles());
            clientDto.setReserves(client.getReserves());
            clientDto.setCel(client.getCel());

            return ResponseEntity.ok().body(clientDto);
        } catch (Exception e) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. En getClientById\"}");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getPersonByUsername( @RequestParam("username") String username ) {
        Optional<Client> client = clientService.getClientByClientName(username);


        if (client.isPresent()) {
            // Clonar el objeto Person y eliminar el campo 'password'
            //Client userWithoutPassword = user.get();
            //userWithoutPassword.setPassword(null);

            ClientDto clientDto = new ClientDto();
            clientDto.setFirstName(client.get().getFirstName());
            clientDto.setLastName(client.get().getLastName());
            clientDto.setClientName(client.get().getClientName());
            clientDto.setId(client.get().getId());
            clientDto.setEmail(client.get().getEmail());
            clientDto.setAddress(client.get().getAddress());
            clientDto.setRoles(client.get().getRoles());
            clientDto.setReserves(client.get().getReserves());
            clientDto.setCel(client.get().getCel());

            // Devolver la respuesta con el objeto modificado
            return ResponseEntity.ok().body(clientDto);
        } else {
            // Manejar el caso en que el usuario no existe
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete( @PathVariable("id") String id ) throws Exception {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
