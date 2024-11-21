//package com.backendIntegrador.config;
//
//import com.backendIntegrador.model.Client;
//import com.backendIntegrador.model.Role;
//import com.backendIntegrador.repository.ClientRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//
//@Component
//public class DataLoader implements ApplicationRunner {
//
//    private final ClientRepository clientRepository;
//
//    @Autowired
//    public DataLoader(ClientRepository clientRepository) {
//        this.clientRepository = clientRepository;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        // Encriptacion de claves
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        String passwordAdmin = bCryptPasswordEncoder.encode("Pru3ba2#");
//        String passwordUser = bCryptPasswordEncoder.encode("pru3ba2#");
//
//        // Usuario ADMIN
//        clientRepository.save(new Client("admin", "prueba", "adminprueba", passwordAdmin, Collections.singleton(Role.ADMIN),"mauroprueba39@gmail.com"));
//
//        // Usuario USER
//        //usuarioRepository.save(new Usuario("Usuario", "user", "usersinfonia@gmail.com", passwordUser, Role.USER));
//
//    }
//
//}