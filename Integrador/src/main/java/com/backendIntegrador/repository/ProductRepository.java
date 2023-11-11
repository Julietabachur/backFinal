package com.backendIntegrador.repository;

import com.backendIntegrador.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    @Query(value = "{'productName' : ?0 }")
    Product checkProductName( String productName );


    Page<Product> findByCategoryIn( Pageable pageable, List<String> categories );

    @Query(value = "{ 'productName': { $regex: ?0, $options: 'i' }, 'reserveIds': { $not: { $elemMatch: { 'startDate': { $lt: ?1 }, 'endDate': { $gt: ?2 } } } } } }")
    Page<Product> searchAvailableProductsByProductNameAndDateRange(
            String productName, LocalDate startDate, LocalDate endDate, Pageable pageable );

}
