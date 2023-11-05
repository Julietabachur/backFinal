package com.backendIntegrador.service;

import com.backendIntegrador.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICategoryService {

    Category save( Category category ) throws Exception;

    boolean delete( String id ) throws Exception;

    Page<Category> findAll( Pageable pageable ) throws Exception;

    List<Category> findAllCategories() throws Exception;

    Category getCategoryById( String id ) throws Exception;

    @Transactional
    Category getCategoryByCategoryName( String categoryName );

    boolean checkCategoryName( String categoryName );

    Category update( Category category ) throws Exception;
}
