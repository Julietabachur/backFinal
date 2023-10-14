package com.backendIntegrador.service;

import com.backendIntegrador.model.Client;

import java.util.List;
import java.util.Optional;

public interface IClientService {
    Client save( Client client ) throws Exception;

    List<Client> clientList() throws Exception;

    Client getClientById( String id ) throws Exception;

    void delete( String id );
}
