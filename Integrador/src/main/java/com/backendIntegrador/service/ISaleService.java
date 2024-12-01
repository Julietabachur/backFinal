package com.backendIntegrador.service;

import com.backendIntegrador.model.Sale;

import java.time.LocalDate;
import java.util.List;

public interface ISaleService {
    Sale save(Sale sale ) throws Exception;

    List<Sale> saleList() throws Exception;

    Sale getSaleById( String id ) throws Exception;

    void delete(String id ) throws Exception;

    List<Sale> findSalesByDateRange(LocalDate startDate, LocalDate endDate) throws Exception;



}
