package com.backendIntegrador.controller;

import com.backendIntegrador.DTO.ClientDto;
import com.backendIntegrador.model.Characteristic;
import com.backendIntegrador.model.Client;
import com.backendIntegrador.service.impl.CharacteristicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {
    @Autowired
    private CharacteristicService characteristicService;


    @GetMapping("/char")
    public ResponseEntity<?> findAllChars( @RequestParam Map<String, Object> params, Model model ) throws Exception {
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



}
