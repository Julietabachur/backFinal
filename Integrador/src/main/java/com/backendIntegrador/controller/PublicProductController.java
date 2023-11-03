package com.backendIntegrador.controller;


import com.backendIntegrador.model.Category;
import com.backendIntegrador.model.Product;
import com.backendIntegrador.model.Type;
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

import java.util.ArrayList;
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
    //@Autowired
   // private CategoryService categoryService;

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


    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById( @PathVariable("id") String id ) {
        try {
            Product product = productService.getProductById(id);

            return ResponseEntity.ok().body(product);
        } catch (Exception e) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. En getProductById\"}");
        }
    }

    @GetMapping("/byType")
    public ResponseEntity<?> findAllByType( @RequestParam Map<String, Object> params, Type type, Model model ) throws Exception {

        int page = params.get("page") != null ? (Integer.parseInt(params.get("page").toString()) - 1) : 0;

        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Product> pageProduct = productService.getProductsByType(pageRequest, type);

        int totalPage = pageProduct.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }

        if (page > totalPage) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. No existe esa pagina\"}");
        }

        model.addAttribute("content", pageProduct.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        return ResponseEntity.ok().body(model);

    }

    @GetMapping("/category")
    public ResponseEntity<?> findAllByCategories(@RequestParam List<String> categories, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        Page<Product> pageProduct = productService.findByCategoryIn(pageRequest, categories);
        System.out.println(pageProduct.getTotalPages());
        if (page >= pageProduct.getTotalPages()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error. No existe esa página.");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", pageProduct.getContent());
        response.put("current", page + 1);
        response.put("next", page + 2);
        response.put("prev", page);
        response.put("last", pageProduct.getTotalPages());

        return ResponseEntity.ok(response);
    }






}
