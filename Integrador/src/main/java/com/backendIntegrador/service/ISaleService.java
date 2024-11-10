package com.backendIntegrador.service;

import com.backendIntegrador.model.Sale;

import java.util.List;

public interface ISaleService {
    Sale save(Sale sale ) throws Exception;

    List<Sale> saleList() throws Exception;

    Sale getSaleById( String id ) throws Exception;

    void delete(String id ) throws Exception;


}
