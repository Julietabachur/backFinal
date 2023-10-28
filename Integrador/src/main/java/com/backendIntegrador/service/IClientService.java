package com.backendIntegrador.service;

import com.backendIntegrador.model.Client;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IClientService {
    Client save( Client client ) throws Exception;

    List<Client> clientList() throws Exception;

    Client getClientById( String id ) throws Exception;

    boolean delete( String id ) throws Exception;

    @Transactional
    Optional<Client> getClientByClientName( String clientName );

    Client checkEmail( String email );

    Client checkClientName( String clientName );

    @Transactional
    Optional<Client> getClientByEmail( String email );
}
