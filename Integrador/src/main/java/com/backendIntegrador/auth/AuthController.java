package com.backendIntegrador.auth;

import com.backendIntegrador.jwt.JwtService;
import com.backendIntegrador.model.Client;
import com.backendIntegrador.service.impl.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173, http://127.0.0.1:5173")
public class AuthController {
    @Autowired
    private final AuthService authService;

    private final ClientService clientService;

    private final JwtService jwtService;

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


    @PutMapping("/verifyToken/{vtk}")
    public ResponseEntity<?> update (@PathVariable("vtk") String verifyToken, @RequestBody String verifiedClient) {
        try {
            // decodifica el token y obtiene el email
            String email = jwtService.getEmailFromToken(verifyToken);
            System.out.println("EMAIL del Token Encontrado");
            System.out.println(email);
            System.out.println("*****************");
            System.out.println("String verified");
            System.out.println(verifiedClient);
            System.out.println("*****************");


            // busca el usuario con el email
            Client verifiedUser = clientService.getClientByEmail(email);
            System.out.println("Usuario Encontrado");
            System.out.println(verifiedUser.getClientName());
            System.out.println("**************************");


            if (verifiedUser == null) {
                // Cliente no encontrado, devuelve un error 404
                return ResponseEntity.notFound().build();
            }

            // confirma la verificación del usuario - isVerified TRUE

            System.out.println("User is Verified");
            System.out.println(verifiedUser.getIsVerified());
            System.out.println("*************************");

            if ("false".equals(verifiedUser.getIsVerified())) {
                verifiedUser.setIsVerified("true");
                Client updated = clientService.update(verifiedUser);
                String token = jwtService.getToken(updated);


                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("client", updated);
                responseMap.put("token", token);

                return ResponseEntity.ok(responseMap);


            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error el usuario ya esta verificado");
            }


        } catch (Exception e) {
            // Maneja cualquier excepción que pueda ocurrir
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la actualización");
        }


    }

}

//import com.backendIntegrador.jwt.JwtService;
//import com.backendIntegrador.model.Client;
//import com.backendIntegrador.service.impl.ClientService;
//import com.backendIntegrador.service.impl.EmailService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/auth")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:5173, http://127.0.0.1:5173")
//public class AuthController {
//    @Autowired
//    private final AuthService authService;
//
//    private final ClientService clientService;
//
//    private final EmailService emailService;
//
//    private final JwtService jwtService;
//
//    @PostMapping(value = "login")
//    public ResponseEntity<?> login( @RequestBody LoginRequest request ) {
//        try{
//            AuthResponse response = authService.login(request);
//            Client client = clientService.getClientByEmail(request.getEmail());
//            // emailService.sendWithImageFromURLLogin("v4lkiria.soporte@gmail.com", request.getEmail(), "Successfully Login", Optional.ofNullable(client));
//            return ResponseEntity.ok(response);
//        }catch (Exception e){
//           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//
//    }
//
//    @PostMapping(value = "register")
//    public ResponseEntity<?> register( @Valid @RequestBody RegisterRequest request ) {
//        try{
//            AuthResponse response = authService.register(request);
//            Client client = clientService.getClientByEmail(request.getEmail());
//            emailService.sendWithImageFromURLRegister("v4lkiria.soporte@gmail.com", request.getEmail(), "Successfully Register", client);
//            return ResponseEntity.ok(response);
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//
//    //--Antes de registrar un usuario chequear primero si ya no existe en el registro--
//    @GetMapping("/clientName")
//    public boolean checkClientName(@RequestParam String clientName){
//        Client user = clientService.checkClientName(clientName);
//        return user == null;
//
//    }
//
//    @GetMapping("/email")
//    public boolean checkEmail(@RequestParam String email){
//        Client user = clientService.checkEmail(email);
//        return user == null;
//
//    }
//
//
//    @PutMapping("/verifyToken/{vtk}")
//    public ResponseEntity<?> update (@PathVariable("vtk") String verifyToken, @RequestBody String verifiedClient) {
//        try {
//            // decodifica el token y obtiene el email
//            String email = jwtService.getEmailFromToken(verifyToken);
//            System.out.println("EMAIL del Token Encontrado");
//            System.out.println(email);
//            System.out.println("*****************");
//            System.out.println("String verified");
//            System.out.println(verifiedClient);
//            System.out.println("*****************");
//
//
//            // busca el usuario con el email
//            Client verifiedUser = clientService.getClientByEmail(email);
//            System.out.println("Usuario Encontrado");
//            System.out.println(verifiedUser.getClientName());
//            System.out.println("**************************");
//
//
//            if (verifiedUser == null) {
//                // Cliente no encontrado, devuelve un error 404
//                return ResponseEntity.notFound().build();
//            }
//
//            // confirma la verificación del usuario - isVerified TRUE
//
//            System.out.println("User is Verified");
//            System.out.println(verifiedUser.getIsVerified());
//            System.out.println("*************************");
//
//            if ("false".equals(verifiedUser.getIsVerified())) {
//                verifiedUser.setIsVerified("true");
//                Client updated = clientService.update(verifiedUser);
//                String token = jwtService.getToken(updated);
//
//
//                Map<String, Object> responseMap = new HashMap<>();
//                responseMap.put("client", updated);
//                responseMap.put("token", token);
//
//                return ResponseEntity.ok(responseMap);
//
//
//            }else{
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error el usuario ya esta verificado");
//            }
//
//
//        } catch (Exception e) {
//            // Maneja cualquier excepción que pueda ocurrir
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la actualización");
//        }
//
//
//    }
//
//}