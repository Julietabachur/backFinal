package com.backendIntegrador.controller;

import com.backendIntegrador.DTO.ClientDto;
import com.backendIntegrador.model.Category;
import com.backendIntegrador.model.Characteristic;
import com.backendIntegrador.model.Client;
import com.backendIntegrador.model.Role;
import com.backendIntegrador.service.impl.CategoryService;
import com.backendIntegrador.service.impl.CharacteristicService;
import com.backendIntegrador.service.impl.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    @Autowired
    private final ClientService clientService;

    @Autowired
    private final CharacteristicService characteristicService;
    @Autowired
    private final CategoryService categoryService;


    @PutMapping("/clients/{id}")
    public ResponseEntity<?> toggleAdminRole( @PathVariable String id ) {
        try {
            // Verifica si el cliente con el ID existe
            Client existingClient = clientService.getClientById(id);
            System.out.println(existingClient);
            if (existingClient == null) {
                // Cliente no encontrado, devuelve un error 404
                return ResponseEntity.notFound().build();
            }

            // Get the current roles of the client
            Set<Role> roles = existingClient.getRoles();

            // Check if the "admin" role is in the client's roles
            boolean hasAdminRole = roles.contains(Role.ADMIN);
            System.out.println(hasAdminRole);
            boolean hasAdminRoleString = roles.contains("USER");

            // Toggle the "admin" role
            if (hasAdminRole) {
                roles.remove(Role.ADMIN); // Remove the "admin" role
                System.out.println("Removed admin role");
            } else {
                roles.add(Role.ADMIN); // Add the "admin" role
                System.out.println("Added admin role");
            }

            // Llama al servicio para realizar la actualización
            // Note: Other fields are not updated here, so they remain unchanged
            Client updated = clientService.update(existingClient);
            System.out.println("Updated client: " + updated);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            // Maneja cualquier excepción que pueda ocurrir
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la actualización");
        }
    }


    @GetMapping("/clients")
    public ResponseEntity<?> findAll( @RequestParam Map<String, Object> params, Model model ) throws Exception {
        int page = params.get("page") != null ? (Integer.parseInt(params.get("page").toString()) - 1) : 0;

        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Client> pageUser = clientService.clientList(pageRequest);

        int totalPage = pageUser.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }
        if (page > totalPage) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. No existe esa pagina\"}");
        }

        List<Client> clientList = pageUser.getContent();
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


        model.addAttribute("content", clientDtoList);
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        return ResponseEntity.ok().body(model);
    }

    @PostMapping("/category" )
    public ResponseEntity<?> saveCategory( @RequestBody Category category ) {

        try {
            return ResponseEntity.ok().body(categoryService.save(category));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. En save\"}");
        }

    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> delete( @PathVariable("id") String id ) throws Exception {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<?> update( @PathVariable String id, @RequestBody Category updatedCategory ) {
        try {
            Category existingCategory = categoryService.getCategoryById(id);

            if (existingCategory == null) {
                return ResponseEntity.notFound().build();
            }
            existingCategory.setCategoryName(updatedCategory.getCategoryName());
            existingCategory.setDescription(updatedCategory.getDescription());
            existingCategory.setImageUrl(updatedCategory.getImageUrl());
            // Llama al servicio para realizar la actualización
            Category updated = categoryService.update(existingCategory);

            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            // Maneja cualquier excepción que pueda ocurrir
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la actualización");
        }
    }

    @PostMapping("/char")
    public ResponseEntity<?> saveChar( @RequestBody Characteristic characteristic ) {
        try {
            return ResponseEntity.ok().body(characteristicService.save(characteristic));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. En save\"}");
        }
    }

    @PutMapping("/char/{id}")
    public ResponseEntity<?> update( @PathVariable String id, @RequestBody Characteristic updatedCharacteristic ) {
        try {
            Characteristic existingCharacteristic = characteristicService.getCharById(id);

            if (existingCharacteristic == null) {
                return ResponseEntity.notFound().build();
            }
            existingCharacteristic.setCharName(updatedCharacteristic.getCharName());
            existingCharacteristic.setCharValue(updatedCharacteristic.getCharValue());
            existingCharacteristic.setCharIcon(updatedCharacteristic.getCharIcon());
            // Llama al servicio para realizar la actualización
            Characteristic updated = characteristicService.update(existingCharacteristic);

            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            // Maneja cualquier excepción que pueda ocurrir
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la actualización");
        }
    }

}
