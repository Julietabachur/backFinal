package com.backendIntegrador.auth;

import com.backendIntegrador.jwt.JwtService;
import com.backendIntegrador.model.Client;
import com.backendIntegrador.model.Role;
import com.backendIntegrador.repository.ClientRepository;
import com.backendIntegrador.service.impl.ClientService;
import com.backendIntegrador.service.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClientRepository clientRepository; // Repositorio para gestionar datos de clientes
    private final ClientService clientService;
    private final JwtService jwtService; // Servicio para trabajar con tokens JWT
    private final PasswordEncoder passwordEncoder; // Encriptador de contraseñas- libreria
    private final AuthenticationManager authenticationManager; // Gestor de autenticación - libreria

    private final EmailService emailService; // Servicio para envio de mails

    // Método para iniciar sesión
    public AuthResponse login( LoginRequest request ) {
        // Autenticar al usuario utilizando el gestor de autenticación
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        // Obtener detalles del usuario desde el repositorio
        Client user = clientRepository.findByEmail(request.getEmail());
        // Generar un token JWT para el usuario autenticado
        String token = jwtService.getToken(user);
        // Devolver una respuesta de autenticación que incluye el token
        return AuthResponse.builder()
                .token(token)
                .build();
    }


    // Método para registrar un nuevo usuario
    public AuthResponse register( RegisterRequest request ) throws Exception {
        // Crear un objeto Cliente con los datos proporcionados en la solicitud
        Client client = Client.builder()
                .clientName(request.getClientName())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .isVerified(false)
                .password(passwordEncoder.encode(request.getPassword())) // Encriptar la contraseña
                .roles(Collections.singleton(Role.USER))// Asignar un rol al usuario (en este caso, USER)
                .build();
        Client checkedName = clientService.checkClientName(request.getClientName());
        Client checkedEmail = clientService.checkEmail(request.getEmail());
        if (checkedName == null && checkedEmail == null) {
            // Guardar el nuevo cliente en el repositorio
            clientRepository.save(client);
        } else {
            throw new Exception("Usuario ya existe");
        }


        // Generar un token JWT para el nuevo usuario y devolverlo como respuesta de registro
        return AuthResponse.builder()
                .token(jwtService.getToken(client))
                .build();
    }

}
