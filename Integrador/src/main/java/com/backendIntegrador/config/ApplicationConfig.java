package com.backendIntegrador.config;



import com.backendIntegrador.repository.ClientRepository;
import com.backendIntegrador.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    // Definición de un bean para AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager( AuthenticationConfiguration config ) throws Exception {
        return config.getAuthenticationManager();
    }

    // Definición de un bean para AuthenticationProvider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService); // Configuración del servicio de detalles de usuario
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Configuración del encriptador de contraseñas
        return authenticationProvider;
    }



    // Definición de un bean para el encriptador de contraseñas (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /*// Definición de un bean para UserDetailsService
    @Bean
    public UserDetailsService userDetailService() {
        return email -> clientRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

*/


}

