package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Client;
import com.backendIntegrador.repository.ClientRepository;
import com.backendIntegrador.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService implements IClientService {
    @Autowired
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService( ClientRepository clientRepository ) {
        this.clientRepository = clientRepository;
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Client save( Client client ) throws Exception {
        try {
            clientRepository.save(client);
            return client;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Page<Client> clientList( Pageable pageable ) throws Exception {
        try {
            return clientRepository.findAll(pageable);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Client getClientById( String id ) throws Exception {
        try {
            return clientRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }


    }

    @Override
    public boolean delete( String id ) throws Exception {
        try {
            if (clientRepository.existsById(id)) {
                clientRepository.deleteById(id);
                return true;
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Client> getClientByClientName( String clientName ) {
        return clientRepository.findByClientName(clientName);
    }


    @Override
    public Client checkEmail( String email ) {
        return clientRepository.checkEmail(email);
    }

    @Override
    public Client checkClientName( String clientName ) {

        return clientRepository.checkClientName(clientName);
    }

    @Override
    public Client getClientByEmail( String email ) {
        return clientRepository.findByEmail(email);
    }


    @Override
    public Client update(Client client) throws ChangeSetPersister.NotFoundException {
        Client existingUser = clientRepository.findById(client.getId())
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        // Actualiza los campos relevantes del usuario con los datos proporcionados
        if ( existingUser != null ) {
            if ( client.getFirstName() != null ) {
                existingUser.setFirstName(client.getFirstName());
            }

            if ( client.getLastName() != null ) {
                existingUser.setLastName(client.getLastName());
            }

            if ( client.getClientName() != null ) {
                existingUser.setClientName(client.getClientName());
            }

            if (client.getPassword() != null && !client.getPassword().isEmpty()) {
                if (!passwordEncoder.matches(client.getPassword(), existingUser.getPassword())) {
                    existingUser.setPassword(passwordEncoder.encode(client.getPassword()));
                }
            } else {
                existingUser.setPassword(existingUser.getPassword());
            }

            if ( client.getRoles() != null ) {
                existingUser.setRoles(client.getRoles());
            }

            if ( client.getIsVerified() != null ) {
                existingUser.setIsVerified(client.getIsVerified());
            }

            if ( client.getEmail() != null ) {
                existingUser.setEmail(client.getEmail());
            }

            if ( client.getCel() != null ) {
                existingUser.setCel(client.getCel());
            }

            if ( client.getAddress() != null ) {
                existingUser.setAddress(client.getAddress());
            }

            if ( client.getFavorites() != null ) {
                existingUser.setFavorites(client.getFavorites());
            }
         }

        // existingUser.setReserveIds(client.getReserveIds());

        // Guarda el usuario actualizado en el repositorio
        return clientRepository.save(existingUser);
    }

    public Client updateWithoutPassword(Client client) throws ChangeSetPersister.NotFoundException {
        Client existingUser = clientRepository.findById(client.getId())
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        if (existingUser != null) {
            // Copia los campos, excluyendo la contraseña
            if (client.getFirstName() != null) {
                existingUser.setFirstName(client.getFirstName());
            }

            if (client.getLastName() != null) {
                existingUser.setLastName(client.getLastName());
            }

            if (client.getClientName() != null) {
                existingUser.setClientName(client.getClientName());
            }

            // NO ACTUALIZAR LA CONTRASEÑA
            // Mantén la contraseña existente en el cliente
            existingUser.setPassword(existingUser.getPassword());

            if (client.getRoles() != null) {
                existingUser.setRoles(client.getRoles());
            }

            if (client.getIsVerified() != null) {
                existingUser.setIsVerified(client.getIsVerified());
            }

            if (client.getEmail() != null) {
                existingUser.setEmail(client.getEmail());
            }

            if (client.getCel() != null) {
                existingUser.setCel(client.getCel());
            }

            if (client.getAddress() != null) {
                existingUser.setAddress(client.getAddress());
            }

            if (client.getFavorites() != null) {
                existingUser.setFavorites(client.getFavorites());
            }
        }

        // Guarda el cliente actualizado
        return clientRepository.save(existingUser);
    }

    ///////Momentaneo para eliminar usuarios para probar registros
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }


}
