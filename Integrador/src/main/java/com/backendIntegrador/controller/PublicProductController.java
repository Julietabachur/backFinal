package com.backendIntegrador.controller;


import com.backendIntegrador.model.Product;
import com.backendIntegrador.model.Type;
import com.backendIntegrador.service.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<?> findAll(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        try {
            Page<Product> productList = productService.productPublicList(pageable);


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

    @GetMapping("/byType/{type}")
    public ResponseEntity<Page<Product>> getProductsByType(@PathVariable Type type, @PageableDefault(page = 0, size = 10)Pageable pageable) throws Exception {
        Page<Product> products = productService.getProductsByType(pageable, type);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }



}
