package com.backendIntegrador.repository;

import com.backendIntegrador.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
