package com.backendIntegrador.controller;


import com.backendIntegrador.model.Product;
import com.backendIntegrador.service.impl.CategoryService;
import com.backendIntegrador.service.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<?> findAll( @RequestParam Map<String, Object> params, Model model ) {
        // int page = params.get("page") != null ? (Integer.parseInt(params.get("page").toString()) - 1) : 0;

        int page = 0;
        if (params.get("page") != null) {
            try {
                page = Integer.parseInt(params.get("page").toString()) - 1;
                if (page < 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("{\"error\":\"El parámetro de página no puede ser menor que 1\"}");
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\":\"El parámetro de página debe ser un número válido\"}");
            }
        }

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

        List<Product> productList = pageProduct.getContent();
        Long totalElements = pageProduct.getTotalElements();


        model.addAttribute("content", productList);
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        model.addAttribute("totalElements", totalElements);
        return ResponseEntity.ok().body(model);
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
    public ResponseEntity<?> findAll(  @RequestParam List<String> categories,@RequestParam Map<String, Object> params, Model model ) {
        int page = params.get("page") != null ? (Integer.parseInt(params.get("page").toString()) - 1) : 0;

        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Product> pageProduct = productService.findByCategoryIn(pageRequest, categories);

        int totalPage = pageProduct.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }
        if (page > totalPage) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. No existe esa pagina\"}");
        }

        // comment

        List<Product> productList = pageProduct.getContent();
        Long totalElements = pageProduct.getTotalElements();


        model.addAttribute("content", productList);
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        model.addAttribute("totalElements", totalElements);
        return ResponseEntity.ok().body(model);

    }

    @GetMapping("/favorites")
    public ResponseEntity<?> GetFavorites( @RequestParam List<String> productIds,@RequestParam Map<String, Object> params, Model model ) {
        int page = params.get("page") != null ? (Integer.parseInt(params.get("page").toString()) - 1) : 0;

        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Product> pageProduct = productService.findByIdIn(productIds, pageRequest);

        int totalPage = pageProduct.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }
        if (page > totalPage) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. No existe esa pagina\"}");
        }

        List<Product> productList = pageProduct.getContent();
        Long totalElements = pageProduct.getTotalElements();


        model.addAttribute("content", productList);
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        model.addAttribute("totalElements", totalElements);
        return ResponseEntity.ok().body(model);

    }


    @GetMapping("/categories")
    public ResponseEntity<?> getProductsByCategoryNames(
            @RequestParam List<String> categoryNames,
            @RequestParam Map<String, Object> params,
            Model model) {

        // Verifica si categoryNames está vacío
        if (categoryNames == null || categoryNames.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Debe proporcionar al menos una categoría.\"}");
        }

        int page = 0;
        if (params.get("page") != null) {
            try {
                page = Integer.parseInt(params.get("page").toString()) - 1;
                if (page < 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("{\"error\":\"El parámetro de página no puede ser menor que 1\"}");
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\":\"El parámetro de página debe ser un número válido\"}");
            }
        }

        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Product> pageProduct = productService.getProductsByCategoryNames(categoryNames, pageRequest);

        int totalPage = pageProduct.getTotalPages();
        if (page >= totalPage) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Error. No existe esa página\"}");
        }

        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }

        List<Product> productList = pageProduct.getContent();
        Long totalElements = pageProduct.getTotalElements();

        model.addAttribute("content", productList);
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        model.addAttribute("totalElements", totalElements);

        return ResponseEntity.ok().body(model);
    }


}
