package com.backendIntegrador.controller;


import com.backendIntegrador.model.Product;
import com.backendIntegrador.service.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/products")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private final ProductService productService;

    @GetMapping("/clientName")
    public boolean checkClientName( @RequestParam String productName ) {
        Product product = productService.checkProductName(productName);
        return product == null;

    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<?> save( @RequestBody Product product ) {
        Product checkedProduct = productService.checkProductName(product.getProductName());
        if (checkedProduct == null) {
            try {
                return ResponseEntity.ok().body(productService.save(product));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. En save\"}");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. nombre de Producto ya existente\"}");
        }

    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('admin:read')")
    public Page<Product> findAll( @PageableDefault(page = 0, size = 10) Pageable pageable ) {
        try {
            return productService.productList(pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> delete( @PathVariable("id") String id ) throws Exception {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
