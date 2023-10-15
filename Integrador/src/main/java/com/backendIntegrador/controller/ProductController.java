package com.backendIntegrador.controller;


import com.backendIntegrador.model.Product;
import com.backendIntegrador.service.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private final ProductService productService;

    @PostMapping("")
    public ResponseEntity<?> save( @RequestBody Product product ) {
        try {
            return ResponseEntity.ok().body(productService.save(product));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. En save\"}");
        }
    }

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        try {
            List<Product> productList = productService.productList();


            return ResponseEntity.ok().body(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error. en Findall");
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById( @PathVariable("id") String id ) {
        try {
            Product product = productService.getProductById(id);

            return ResponseEntity.ok().body(product);
        } catch (Exception e) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. En getProductById\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete( @PathVariable("id") String id ) throws Exception {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
