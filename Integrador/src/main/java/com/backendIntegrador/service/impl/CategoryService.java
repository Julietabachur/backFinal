package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Category;
import com.backendIntegrador.repository.CategoryRepository;
import com.backendIntegrador.service.ICategoryService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Validator validator;

    @Override
    @Transactional
    public Category save( Category category ) throws Exception {
        try {
            Set<ConstraintViolation<Category>> violations = validator.validate(category);

            if (!violations.isEmpty()) {
                // Handle validation errors
                StringBuilder errorMessage = new StringBuilder("Validation errors: ");
                for (ConstraintViolation<Category> violation : violations) {
                    errorMessage.append(violation.getMessage()).append(", ");
                }
                throw new Exception(errorMessage.toString());
            }

            Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
            if (existingCategory != null) {
                // La categoría ya existe, puedes manejarlo según tus necesidades, como lanzar una excepción o actualizar la categoría existente.
                // Por ejemplo, para lanzar una excepción, puedes hacer lo siguiente:
                throw new Exception("La categoría ya existe en la base de datos.");
            } else {
                // La categoría no existe, puedes guardarla y retornarla.
                categoryRepository.save(category);
                return category;
            }
        } catch (Exception e) {
            // Maneja la excepción de manera apropiada, por ejemplo, registrándola o lanzando una excepción personalizada.
            throw new Exception("Error al guardar la categoría: " + e.getMessage());
        }
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
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> findAllCategories() throws Exception {
        return categoryRepository.findAll();
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

            Set<ConstraintViolation<Category>> violations = validator.validate(category);

            if (!violations.isEmpty()) {
                // Handle validation errors
                StringBuilder errorMessage = new StringBuilder("Validation errors: ");
                for (ConstraintViolation<Category> violation : violations) {
                    errorMessage.append(violation.getMessage()).append(", ");
                }
                throw new Exception(errorMessage.toString());
            }
            Category existingCategoryById = categoryRepository.findById(category.getId()).orElse(null);
            Category existingCategoryByName = categoryRepository.findByCategoryName(category.getCategoryName());

            if (existingCategoryById != null && existingCategoryByName == null) {
                existingCategoryById.setCategoryName(category.getCategoryName());
                existingCategoryById.setDescription(category.getDescription());
                existingCategoryById.setImageUrl(category.getImageUrl());

                return categoryRepository.save(existingCategoryById);
            }else if(existingCategoryByName != null){
                throw new RuntimeException("Ya existe ese nombre en otra categoria");
            } else {
                throw new RuntimeException("La categoria no se encontró para la actualización");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
