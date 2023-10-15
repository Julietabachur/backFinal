package com.backendIntegrador.repository;

import com.backendIntegrador.model.Reserve;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReserveRepository extends MongoRepository<Reserve, String> {
}
