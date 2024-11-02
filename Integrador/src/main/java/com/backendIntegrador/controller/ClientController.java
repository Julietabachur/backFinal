package com.backendIntegrador.controller;


import com.backendIntegrador.DTO.ClientDto;
import com.backendIntegrador.model.Client;
import com.backendIntegrador.service.impl.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
            response.put("email", client.getEmail());
            response.put("roles", client.getRoles());

            response.put("favorites",client.getFavorites());
            response.put("isVerified", client.getIsVerified()); // envia el String de verificado o no.

            response.put("id", client.getId()); // envia el ID.

            // Add other user data as needed

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error. en getme");
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
            clientDto.setIsVerified(client.getIsVerified()); // agregado string de usuario verificado.
            clientDto.setReserveIds(client.getReserveIds());
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
            ClientDto clientDto = new ClientDto();
            clientDto.setFirstName(client.get().getFirstName());
            clientDto.setLastName(client.get().getLastName());
            clientDto.setClientName(client.get().getClientName());
            clientDto.setId(client.get().getId());
            clientDto.setEmail(client.get().getEmail());
            clientDto.setAddress(client.get().getAddress());
            clientDto.setRoles(client.get().getRoles());
            clientDto.setIsVerified(client.get().getIsVerified());  // agregado booleano de usuario verificado.
            clientDto.setReserveIds(client.get().getReserveIds());
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

    //método para actualizar en el cliente solo con los datos que quiero cambiar y mantiene los otros,
    // sirve lo mismo para favoritos o para cualquier otro filds que quiera modificar
    @PutMapping("/{id}")
    public ResponseEntity<?> update( @PathVariable String id, @RequestBody Client updatedClient ) {
        try {
            // Verifica si el cliente con el ID existe
            Client existingClient = clientService.getClientById(id);

            if (existingClient == null) {
                // Cliente no encontrado, devuelve un error 404
                return ResponseEntity.notFound().build();
            }

            // Obtén la clase del objeto Cliente
            Class<?> clientClass = Client.class;

            // Obtén todos los campos de la clase
            Field[] fields = clientClass.getDeclaredFields();

            // Itera sobre los campos
            for (Field field : fields) {
                // Hace accesible el campo para poder establecer su valor
                field.setAccessible(true);

                // Obtiene el valor actual del campo en existingClient
                Object existingValue = field.get(existingClient);

                // Obtiene el valor actualizado de updatedClient
                Object updatedValue = field.get(updatedClient);

                // Si el valor actualizado no es nulo, actualiza el campo en existingClient
                if (updatedValue != null) {
                    field.set(existingClient, updatedValue);
                } else {
                    // Si el valor actualizado es nulo, pero el valor actual en existingClient no lo es, mantén el valor actual
                    if (existingValue != null) {
                        field.set(existingClient, existingValue);
                    }
                }
            }

            // Llama al servicio para realizar la actualización
            Client updated = clientService.update(existingClient);

            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            // Maneja cualquier excepción que pueda ocurrir
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la actualización");
        }
    }

    /*
    @PutMapping("/chk/{id}")
    //Modifica el booleano isVerified en el objeto cliente. Evita pasar todos los datos del usuario.
    public ResponseEntity<?> update( @PathVariable String id ) {
        try {
            // Verifica si el usuario con el ID existe
            Client existingUser = clientService.getClientById(id);

            if (existingUser == null) {
                // Usuario no encontrado, devuelve un error 404
                return ResponseEntity.notFound().build();
            }
            System.out.println(existingUser.isVerified());
            // Actualiza los campos relevantes del usuario con los datos proporcionados
            if (!existingUser.isVerified()) {
                existingUser.setVerified(true);
                Client updated = clientService.update(existingUser);

                return ResponseEntity.ok(updated);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error el usuario ya esta verificado");
            }
            // Llama al servicio para realizar la actualización


        } catch (Exception e) {
            // Maneja cualquier excepción que pueda ocurrir
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la actualización");
        }
    }

     */

}
