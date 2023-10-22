package com.backendIntegrador.service;

import com.backendIntegrador.model.Product;
import com.backendIntegrador.model.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    Product save( Product product ) throws Exception;

    /*List<Product> productList() throws Exception;*/

    Page<Product> productList( Pageable pageable ) throws Exception;

    Product getProductById( String id ) throws Exception;

    boolean delete( String id ) throws Exception;


    List<Product> productPublicList();

    Product checkProductName( String productName );

    Product editProduct(String id, Product product) throws Exception;


    List<Product> getProductsByType( Type type );
}
