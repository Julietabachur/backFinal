package com.backendIntegrador.repository;

import com.backendIntegrador.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {

    Optional<Client> findByClientName( String clientName );

    @Query(value = "{'clientName' : ?0 }")
    Client checkClientName( String clientName );

    @Query(value = "{'email' : ?0 }")
    Client checkEmail( String email );

    @Query(value = "{'email' : ?0 }")
    Optional<Client>  findByEmail( String email );
}
