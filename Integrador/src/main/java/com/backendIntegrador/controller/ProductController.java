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
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private final ProductService productService;

    @GetMapping("/productName")
    public boolean checkProductName( @RequestParam String productName ) {
        Product product = productService.checkProductName(productName);
        return product == null;

    }

    @PostMapping("")
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
    public Page<Product> findAll( @PageableDefault(page = 0, size = 10) Pageable pageable ) {
        try {
            return productService.productList(pageable);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update( @PathVariable String id, @RequestBody Product updatedProduct ) {
        try {
            // Verifica si el producto con el ID existe
            Product existingProduct = productService.getProductById(id);

            if (existingProduct == null) {
                // Producto no encontrado, devuelve un error 404
                return ResponseEntity.notFound().build();
            }

            // Actualiza los campos relevantes del producto con los datos proporcionados
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setProductionTime(updatedProduct.getProductionTime());
            existingProduct.setCollection(updatedProduct.getCollection());
            existingProduct.setCustomCollection(updatedProduct.getCustomCollection());
            existingProduct.setDetail(updatedProduct.getDetail());
            existingProduct.setProductSize(updatedProduct.getProductSize());
            existingProduct.setType(updatedProduct.getType());
            existingProduct.setThumbnail(updatedProduct.getThumbnail());
            existingProduct.setGallery(updatedProduct.getGallery());

            // Llama al servicio para realizar la actualización
            Product updated = productService.update(existingProduct);

            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            // Maneja cualquier excepción que pueda ocurrir
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la actualización");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete( @PathVariable("id") String id ) throws Exception {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
