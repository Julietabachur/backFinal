package com.backendIntegrador.controller;

import com.backendIntegrador.model.Category;
import com.backendIntegrador.model.Characteristic;
import com.backendIntegrador.model.Product;
import com.backendIntegrador.model.Reserve;
import com.backendIntegrador.service.impl.CategoryService;
import com.backendIntegrador.service.impl.CharacteristicService;
import com.backendIntegrador.service.impl.ProductService;
import com.backendIntegrador.service.impl.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {
    @Autowired
    private CharacteristicService characteristicService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReserveService reserveService;


    @GetMapping("/products/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam Map<String, Object> params,
            Model model
    ) {
        try {
            int page = params.get("page") != null ? (Integer.parseInt(params.get("page").toString()) - 1) : 0;
            // Establecer valores predeterminados si no se proporcionan fechas
            if (startDate == null) {
                startDate = LocalDate.now(); // Fecha actual como valor predeterminado
            }
            if (endDate == null) {
                endDate = LocalDate.of(2024, 12, 1);
            }

            PageRequest pageable = PageRequest.of(page, 10);
            Page<Product> results = productService.searchProductsByProductNameAndDateRange(productName, startDate, endDate, pageable);

            int totalPage = results.getTotalPages();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                model.addAttribute("pages", pages);
            }
            if (page > totalPage) {
                return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. No existe esa pagina\"}");
            }

            List<Product> productList = results.getContent();
            Long totalElements = results.getTotalElements();

            model.addAttribute("content", productList);
            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);
            model.addAttribute("totalElements", totalElements);

            return ResponseEntity.ok().body(model);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/category")
    public ResponseEntity<?> findAllCategories( @RequestParam Map<String, Object> params, Model model ) throws Exception {
        int page = params.get("page") != null ? (Integer.parseInt(params.get("page").toString()) - 1) : 0;

        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Category> pageCategories = categoryService.findAll(pageRequest);

        int totalPage = pageCategories.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }
        if (page > totalPage) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. No existe esa pagina\"}");
        }

        List<Category> categories = pageCategories.getContent();

        model.addAttribute("content", categories);
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        return ResponseEntity.ok().body(model);
    }

    @GetMapping("/category/all")
    public ResponseEntity<?> findAllPagesCategory() throws Exception {

        List<Category> categories = categoryService.findAllCategories();


        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getCategoryById( @PathVariable("id") String id ) {
        try {
            Category category = categoryService.getCategoryById(id);

            return ResponseEntity.ok().body(category);
        } catch (Exception e) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. En getCategoryById\"}");
        }
    }


    @GetMapping("/char")
    public ResponseEntity<?> findAllPagesChars( @RequestParam Map<String, Object> params, Model model ) throws Exception {
        int page = params.get("page") != null ? (Integer.parseInt(params.get("page").toString()) - 1) : 0;

        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Characteristic> pageChar = characteristicService.findAll(pageRequest);

        int totalPage = pageChar.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }
        if (page > totalPage) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. No existe esa pagina\"}");
        }

        List<Characteristic> charList = pageChar.getContent();


        model.addAttribute("content", charList);
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        return ResponseEntity.ok().body(model);
    }

    @GetMapping("/char/all")
    public ResponseEntity<?> findAllChars() throws Exception {

        List<Characteristic> characteristic = characteristicService.findAllChars();


        return ResponseEntity.ok().body(characteristic);
    }

    @GetMapping("/char/{id}")
    public ResponseEntity<?> getCharById( @PathVariable("id") String id ) {
        try {
            Characteristic characteristic = characteristicService.getCharById(id);

            return ResponseEntity.ok().body(characteristic);
        } catch (Exception e) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. En getCharById\"}");
        }
    }

    @GetMapping("/reserves/search/byProductId")
    public ResponseEntity<?> getReserveByProductId( @RequestParam("productId") String productId ) {
        try {
            List<Reserve> reserve = reserveService.getReserveByProductId(productId);

            return ResponseEntity.ok().body(reserve);
        } catch (Exception e) {
            return (ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. En getReserveById\"}"));
        }
    }


}
