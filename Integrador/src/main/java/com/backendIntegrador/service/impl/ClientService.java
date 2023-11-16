package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Client;
import com.backendIntegrador.repository.ClientRepository;
import com.backendIntegrador.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        existingUser.setFirstName(client.getFirstName());
        existingUser.setLastName(client.getLastName());
        existingUser.setClientName(client.getClientName());
        existingUser.setPassword(client.getPassword());
        existingUser.setRoles(client.getRoles());
        existingUser.setVerified(client.isVerified());
        existingUser.setEmail(client.getEmail());
        existingUser.setCel(client.getCel());
        existingUser.setAddress(client.getAddress());
        existingUser.setReserveIds(client.getReserveIds());
        existingUser.setFavorites(client.getFavorites());

        // Guarda el usuario actualizado en el repositorio
        return clientRepository.save(existingUser);
    }



}
