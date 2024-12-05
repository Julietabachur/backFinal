package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Sale;
import com.backendIntegrador.repository.SaleRepository;
import com.backendIntegrador.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class SaleService implements ISaleService {
    @Autowired
    private SaleRepository saleRepository;
    @Override
    public Sale save(Sale sale) throws Exception {

        return saleRepository.save(sale);
    }


    @Override
    public List<Sale> saleList() throws Exception {
        return saleRepository.findAll();
    }

    @Override
    public Sale getSaleById(String id) throws Exception {
        return saleRepository.findById(id).orElse(null);
    }

    //agregar bolenano para verticar que exista
    @Override
    public void delete(String id) throws Exception {
         saleRepository.deleteById(id);
    }

    @Override
    public List<Sale> findSalesByDateRange(LocalDate startDate, LocalDate endDate) throws Exception {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // Final del día
        return saleRepository.findAllByDateRange(startDateTime, endDateTime);
    }
}
