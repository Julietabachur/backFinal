package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Product;
import com.backendIntegrador.repository.ProductRespository;
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
    private final ProductRespository productRespository;

    @Autowired
    private final ProductIdService productIdService = null;

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
            Long generatedProductId = productIdService.getNextSequence("product");
            product.setProductId(generatedProductId);
            productRespository.save(product);
            return product;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Page<Product> productList( Pageable pageable ) throws Exception {
        try {
            return productRespository.findAll(pageable);
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

    @Override
    public List<Product> productPublicList() {
        return productRespository.findAll();
    }

    @Override
    public Product checkProductName( String productName ) {
        return productRespository.checkProductName(productName);
    }


}
