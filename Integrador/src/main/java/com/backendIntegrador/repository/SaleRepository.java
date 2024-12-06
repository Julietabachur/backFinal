package com.backendIntegrador.repository;
import com.backendIntegrador.model.Sale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends MongoRepository<Sale, String> {

    @Query("{ 'saleDate': { $gte: ?0, $lte: ?1 } }")
    List<Sale> findAllByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime);

    Optional<List<Sale>> findByUserId (String userId);

}
