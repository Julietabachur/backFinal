package com.backendIntegrador.repository;

import com.backendIntegrador.model.Category;
import com.backendIntegrador.model.Characteristic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    @Query(value = "{'categoryName' : ?0 }")
    Category findByCategoryName( String categoryName );

    List<Category> findByCategoryNameIn( List<String> categoryNames );
}
