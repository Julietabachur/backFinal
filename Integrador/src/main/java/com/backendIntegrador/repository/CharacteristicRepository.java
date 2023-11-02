package com.backendIntegrador.repository;

import com.backendIntegrador.model.Characteristic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface CharacteristicRepository extends MongoRepository<Characteristic, String> {
    @Query(value = "{'charName' : ?0 }")
    Characteristic findByCharName( String charName );
}
