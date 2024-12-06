package com.backendIntegrador.repository;
import com.backendIntegrador.model.Sale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends MongoRepository<Sale, String> {

    Optional<List<Sale>> findByUserId (String userId);

}
