package com.backendIntegrador.controller;

import com.backendIntegrador.DTO.ClientDto;
import com.backendIntegrador.model.Client;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    @Autowired
    private final ClientService clientService;


    @PutMapping("/clients/{id}")
    public ResponseEntity<?> update( @PathVariable String id, @RequestBody Client updatedClient ) {
        try {
            // Verifica si el producto con el ID existe
            Client existingUser = clientService.getClientById(id);

            if (existingUser == null) {
                // Producto no encontrado, devuelve un error 404
                return ResponseEntity.notFound().build();
            }

            // Actualiza los campos relevantes del producto con los datos proporcionados
            existingUser.setRoles(updatedClient.getRoles());

            // Llama al servicio para realizar la actualización
            Client updated = clientService.update(existingUser);

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
        if(page > totalPage){
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
}
