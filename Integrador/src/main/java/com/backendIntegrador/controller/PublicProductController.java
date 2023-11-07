package com.backendIntegrador.controller;


import com.backendIntegrador.model.Product;
import com.backendIntegrador.model.Type;
import com.backendIntegrador.service.impl.CategoryService;
import com.backendIntegrador.service.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/v1/public/products")
@RequiredArgsConstructor
public class PublicProductController {
    @Autowired
    private final ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<?> findAll( @RequestParam(defaultValue = "0") int page ) {

        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        Page<Product> pageProduct = productService.getAll(pageRequest);

        if (page >= pageProduct.getTotalPages()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error. No existe esa página.");
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", pageProduct.getContent());
        response.put("current", page + 1);
        response.put("next", page + 2);
        response.put("prev", page);
        response.put("last", pageProduct.getTotalPages());
        response.put("totalItems", pageProduct.getTotalElements());
        return ResponseEntity.ok().body(response);
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

    @GetMapping("/category")
    public ResponseEntity<?> findAllByCategories(
            @RequestParam List<String> categories,
            @RequestParam(defaultValue = "0") int page
    ) {
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        if (categories.isEmpty()) {
            // Handle empty categories, return an error response or a meaningful message.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Categories cannot be empty.");
        }

        Page<Product> pageProduct = productService.findByCategoryIn(pageRequest, categories);

        if (page >= pageProduct.getTotalPages()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error. No existe esa página.");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", pageProduct.getContent());
        response.put("current", page + 1);
        response.put("next", page + 2);
        response.put("prev", page);
        response.put("last", pageProduct.getTotalPages());
        response.put("totalItems", pageProduct.getTotalElements());

        return ResponseEntity.ok(response);
    }




}
