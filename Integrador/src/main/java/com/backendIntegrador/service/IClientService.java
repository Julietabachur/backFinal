package com.backendIntegrador.service;

import com.backendIntegrador.model.Client;
import com.backendIntegrador.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IClientService {
    Client save( Client client ) throws Exception;

    Page<Client> clientList( Pageable pageable ) throws Exception;

    Client getClientById( String id ) throws Exception;

    boolean delete( String id ) throws Exception;

    @Transactional
    Optional<Client> getClientByClientName( String clientName );

    Client checkEmail( String email );

    Client checkClientName( String clientName );

    @Transactional
    Client getClientByEmail( String email );

    Client update( Client client ) throws Exception;


}
