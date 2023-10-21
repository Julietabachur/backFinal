package com.backendIntegrador.controller;


import com.backendIntegrador.model.Product;
import com.backendIntegrador.service.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/public/products")
@RequiredArgsConstructor
public class PublicProductController {
    @Autowired
    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        try {
            List<Product> productList = productService.productPublicList();
            Collections.shuffle(productList);

            return ResponseEntity.ok().body(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error. en Findall");
        }

    }
    @GetMapping("/random")
    public ResponseEntity<?> findTenRandomProducts() {
        try {
            List<Product> productList = productService.productPublicList();

            // Shuffle the entire productList to randomize the order
            Collections.shuffle(productList);

            // Take the first 10 products to get a random selection
            List<Product> randomProducts = productList.subList(0, Math.min(productList.size(), 10));

            return ResponseEntity.ok().body(randomProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error in findRandomProducts");
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


}
