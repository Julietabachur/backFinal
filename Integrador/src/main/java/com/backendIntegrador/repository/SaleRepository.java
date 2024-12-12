package com.backendIntegrador.repository;
import com.backendIntegrador.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends MongoRepository<Sale, String> {

    @Query("{ 'saleDate': { $gte: ?0, $lte: ?1 } }")
    List<Sale> findAllByDateRangeWithoutPage(LocalDateTime startDateTime, LocalDateTime endDateTime);
    @Query("{ 'saleDate': { $gte: ?0, $lte: ?1 } }")
    Page<Sale> findAllByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    Optional<List<Sale>> findByIdUser (String idUser);

}
