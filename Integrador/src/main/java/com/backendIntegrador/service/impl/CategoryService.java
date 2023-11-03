package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Category;
import com.backendIntegrador.repository.CategoryRepository;
import com.backendIntegrador.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category save( Category category ) throws Exception {
        try {

            Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
            if (existingCategory == null) {
                categoryRepository.save(category);

            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return category;
    }

    @Override
    public boolean delete( String id ) throws Exception {
        try {
            if (categoryRepository.existsById(id)) {
                categoryRepository.deleteById(id);
                return true;
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return false;
    }

    @Override
    public Page<Category> findAll( Pageable pageable ) throws Exception {
        return null;
    }

    @Override
    public Category getCategoryById( String id ) throws Exception {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category getCategoryByCategoryName( String categoryName ) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    @Override
    public boolean checkCategoryName( String categoryName ) {
        Category existingCategory = categoryRepository.findByCategoryName(categoryName);
        return existingCategory == null;
    }

    @Override
    public Category update( Category category ) throws Exception {
        try {
            Category existingCategory = categoryRepository.findById(category.getId()).orElse(null);

            if (existingCategory != null) {
                existingCategory.setCategoryName(category.getCategoryName());
                existingCategory.setDescription(category.getDescription());
                existingCategory.setImageUrl(category.getImageUrl());

                return categoryRepository.save(existingCategory);
            } else {
                throw new RuntimeException("La categoria no se encontró para la actualización");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
