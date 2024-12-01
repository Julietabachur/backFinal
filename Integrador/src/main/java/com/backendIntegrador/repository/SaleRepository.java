package com.backendIntegrador.repository;

import com.backendIntegrador.model.Reserve;
import com.backendIntegrador.model.Sale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface SaleRepository extends MongoRepository<Sale, String> {


        /*@Query("{ 'date': { $gte: ?0, $lte: ?1 } }")
        List<Sale> findAllByDateRange(LocalDate startDate, LocalDate endDate);*/

        @Query("{ 'saleDate': { $gte: ?0, $lte: ?1 } }")
        List<Sale> findAllByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime);


}
