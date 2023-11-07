package com.backendIntegrador.service;

import com.backendIntegrador.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    Product save( Product product ) throws Exception;

    Page<Product> productList( Pageable pageable ) throws Exception;

    Product getProductById( String id ) throws Exception;

    boolean delete( String id ) throws Exception;

    Page<Product> getAll( Pageable pageable );

    Product checkProductName( String productName );


    Product update( Product existingProduct ) throws Exception;

    Page<Product> findByCategoryIn( Pageable pageable, List<String> categories );

}
