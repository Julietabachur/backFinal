package com.backendIntegrador.service;

import com.backendIntegrador.model.Product;

import java.util.List;

public interface IProductService {
    Product save(Product product) throws Exception;

    List<Product> productList() throws Exception;

    Product getProductById( String id ) throws Exception;

    boolean delete( String id ) throws Exception;
}
