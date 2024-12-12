package com.backendIntegrador.service;

import com.backendIntegrador.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ISaleService {
    Sale save(Sale sale ) throws Exception;

    List<Sale> saleList() throws Exception;

    List<Sale> saleListByIdUser( String idUser ) throws Exception;

    Sale getSaleById( String id ) throws Exception;

    void delete(String id ) throws Exception;

    List<Sale> findAllByDateRangeWithoutPage(LocalDate startDate, LocalDate endDate) throws Exception;

    Page<Sale> findSalesByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) throws Exception;


}
