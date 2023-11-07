package com.backendIntegrador.controller;


import com.backendIntegrador.model.Category;
import com.backendIntegrador.model.Product;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final CategoryService categoryService;


    @GetMapping("/productName")
    public boolean checkProductName( @RequestParam String productName ) {
        Product product = productService.checkProductName(productName);
        return product == null;

    }

    @PostMapping("")
    public ResponseEntity<?> save( @RequestBody Product product ) {
        Product checkedProduct = productService.checkProductName(product.getProductName());

        Category category = categoryService.getCategoryByCategoryName(product.getCategory());

        if (checkedProduct == null && category != null) {
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
    public ResponseEntity<?> findAll( @RequestParam Map<String, Object> params, Model model ) {
        int page = params.get("page") != null ? (Integer.parseInt(params.get("page").toString()) - 1) : 0;

        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Product> pageProduct = productService.getAll(pageRequest);

        int totalPage = pageProduct.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }
        if (page > totalPage) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. No existe esa pagina\"}");
        }

        List<Product> shuffledList = pageProduct.getContent();


        model.addAttribute("content", shuffledList);
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        return ResponseEntity.ok().body(model);
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
            existingProduct.setType(updatedProduct.getType());
            existingProduct.setThumbnail(updatedProduct.getThumbnail());
            existingProduct.setGallery(updatedProduct.getGallery());
            existingProduct.setFeatures(updatedProduct.getFeatures());
            existingProduct.setCategory(updatedProduct.getCategory());

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
