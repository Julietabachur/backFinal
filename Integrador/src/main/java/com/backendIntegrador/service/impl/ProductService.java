package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Product;
import com.backendIntegrador.repository.ProductRespository;
import com.backendIntegrador.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    private final ProductRespository productRespository;

    @Autowired
    public ProductService( ProductRespository productRespository ) {
        this.productRespository = productRespository;
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Product save( Product product ) throws Exception {
        try {
            productRespository.save(product);
            return product;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Product> productList() throws Exception {
        try {
            return productRespository.findAll();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public Product getProductById( String id ) throws Exception {
        try {
            return productRespository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public boolean delete( String id ) throws Exception {
        try {
            if (productRespository.existsById(id)) {
                productRespository.deleteById(id);
                return true;
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return false;
    }
}
