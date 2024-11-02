package com.backendIntegrador.repository;

import com.backendIntegrador.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    @Query(value = "{'productName' : ?0 }")
    Product checkProductName( String productName );


    Page<Product> findByCategoryIn( Pageable pageable, List<String> categories );

    @Query(value = "{ 'productName': { $regex: ?0, $options: 'i' }, " +
            "'reserveIds': { $not: { $in: ?1 } } } }")
    Page<Product> searchAvailableProductsByProductNameAndDateRange(
            String productName, List<String> reservedIds, Pageable pageable );

    List<Product> findByProductNameRegexIgnoreCase( String productName );

    Page<Product> findByIdIn( List<String> productId, Pageable pageable );


    @Query(value = "{'category' : { $in: ?0 }}")
    Page<Product> findByCategoryNames(List<String> categoryNames, Pageable pageable);

}
