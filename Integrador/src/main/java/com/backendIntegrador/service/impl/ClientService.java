package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Client;
import com.backendIntegrador.repository.ClientRepository;
import com.backendIntegrador.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService implements IClientService {
    @Autowired
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository){this.clientRepository = clientRepository;}

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Client save( Client client ) throws Exception{
        try{
            clientRepository.save(client);
            return client;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<Client> clientList() throws Exception {
        try {
            return clientRepository.findAll();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Client getClientById( String id ) throws Exception {
        try{
            return clientRepository.findById(id).orElse(null);
        } catch(Exception e) {
                throw new Exception(e.getMessage());
        }


    }

    @Override
    public void delete( String id ) {

    }


}
