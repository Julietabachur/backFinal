package com.backendIntegrador.controller;

import com.backendIntegrador.model.Sale;
import com.backendIntegrador.service.impl.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/private/sales")
@RequiredArgsConstructor
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping("")
    public Sale create (@RequestBody Sale sale) throws Exception {
        return saleService.save(sale);
    }

    @GetMapping("/{id}")
    public Sale getsale (@PathVariable String id) throws Exception {
        return saleService.getSaleById(id);
    }

    @GetMapping("")
    public List<Sale> saleList() throws Exception {
        return saleService.saleList();
    }

    @GetMapping("/user/{userId}")
    public List<Sale> saleListByUserId(@PathVariable String userId) throws Exception {
        return saleService.saleListByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteSale (@PathVariable String id) throws Exception {
        saleService.delete(id);
    }

    
}
