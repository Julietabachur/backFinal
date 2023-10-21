package com.backendIntegrador.repository;

import com.backendIntegrador.model.Product;
import com.backendIntegrador.model.Type;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    @Query(value = "{'productName' : ?0 }")
    Product checkProductName( String productName );

    List<Product> findByType( Type type );
}
