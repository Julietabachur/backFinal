package com.backendIntegrador.auth;

import com.backendIntegrador.model.Client;
import com.backendIntegrador.service.impl.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173, http://127.0.0.1:5173")
public class AuthController {
    @Autowired
    private final AuthService authService;

    private final ClientService clientService;

    @PostMapping(value = "login")
    public ResponseEntity<?> login( @RequestBody LoginRequest request ) {
        try{
            return ResponseEntity.ok(authService.login(request));
        }catch (Exception e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping(value = "register")
    public ResponseEntity<?> register( @Valid @RequestBody RegisterRequest request ) {
        try{
            return ResponseEntity.ok(authService.register(request));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    //--Antes de registrar un usuario chequear primero si ya no existe en el registro--
    @GetMapping("/clientName")
    public boolean checkClientName(@RequestParam String clientName){
        Client user = clientService.checkClientName(clientName);
        return user == null;

    }

    @GetMapping("/email")
    public boolean checkEmail(@RequestParam String email){
        Client user = clientService.checkEmail(email);
        return user == null;

    }
}