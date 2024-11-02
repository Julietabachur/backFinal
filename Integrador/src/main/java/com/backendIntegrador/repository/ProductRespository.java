package com.backendIntegrador.repository;

import com.backendIntegrador.model.Client;
import com.backendIntegrador.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRespository extends MongoRepository<Product, String> {
    @Query(value = "{'productName' : ?0 }")
    Product checkProductName( String productName );
}
