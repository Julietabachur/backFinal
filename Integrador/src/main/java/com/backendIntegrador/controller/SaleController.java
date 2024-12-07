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

    @Autowired
    private EmailController emailController;

    @PostMapping("")
    public Sale create (@RequestBody Sale sale) throws Exception {
        emailController.sendNotificationSale(sale);
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

    @GetMapping("/user/{idUser}")
    public List<Sale> saleListByIdUser(@PathVariable String idUser) throws Exception {
        return saleService.saleListByIdUser(idUser);
    }

    @DeleteMapping("/{id}")
    public void deleteSale (@PathVariable String id) throws Exception {
        saleService.delete(id);
    }

    
}
