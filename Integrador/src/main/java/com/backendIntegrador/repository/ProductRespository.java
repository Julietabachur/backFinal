package com.backendIntegrador.repository;

import com.backendIntegrador.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRespository extends MongoRepository<Product, String> {
}
