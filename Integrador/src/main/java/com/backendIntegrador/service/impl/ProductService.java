package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Category;
import com.backendIntegrador.model.Product;
import com.backendIntegrador.model.Type;
import com.backendIntegrador.repository.CategoryRepository;
import com.backendIntegrador.repository.ProductRepository;
import com.backendIntegrador.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private final ProductIdService productIdService = null;

    @Autowired
    public ProductService( ProductRepository productRepository ) {
        this.productRepository = productRepository;
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Product save( Product product ) throws Exception {
        try {
            Long generatedProductId = productIdService.getNextSequence("product");
            product.setProductId(generatedProductId);
            productRepository.save(product);
            return product;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }



    @Override
    public Page<Product> productList( Pageable pageable ) throws Exception {
        try {
            return productRepository.findAll(pageable);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    public Page<Product> getProductsByType( Pageable pageable, Type type ) throws Exception {
        try {
            return productRepository.findByType(pageable, type);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public Product update( Product product ) throws Exception {
        try {
            Product existingProduct = productRepository.findById(product.getId()).orElse(null);

            if (existingProduct != null) {
                Product updatedProduct = productRepository.save(product);
                return updatedProduct;
            } else {
                throw new RuntimeException("El producto no se encontró para la actualización");
            }

            // Actualiza el producto en la base de datos


        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public Page<Product> findByCategoryIn( Pageable pageable, List<String> categories ) {
        return productRepository.findByCategoryIn(pageable,categories);
    }


    @Override
    public Product getProductById( String id ) throws Exception {
        try {
            return productRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public boolean delete( String id ) throws Exception {
        try {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
                return true;
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return false;


    }

    @Override
    public Page<Product> getAll(Pageable pageable){

        return productRepository.findAll(pageable);
    }



    @Override
    public Product checkProductName( String productName ) {
        return productRepository.checkProductName(productName);
    }


    @Override
    public Page<Product> filterProductsByCategoryNames( List<String> categoryNames, Pageable pageable) {
        // Crea una lista de categorías con los nombres proporcionados
        List<Category> categories = categoryRepository.findByCategoryNameIn(categoryNames);

        // Realiza la consulta paginada
        return productRepository.findByCategoriesIn(categories, pageable);
    }


}
