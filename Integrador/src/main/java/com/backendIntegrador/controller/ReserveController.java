package com.backendIntegrador.controller;

import com.backendIntegrador.model.Reserve;
import com.backendIntegrador.service.impl.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/private/reserves")
@RequiredArgsConstructor
public class ReserveController {


    @Autowired
    private final ReserveService reserveService;

    @PostMapping("")
    public ResponseEntity<?> save( @RequestBody Reserve reserve ) {
        try {
            return ResponseEntity.ok().body(reserveService.save(reserve));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. En save\"}");
        }
    }

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        try {
            List<Reserve> reserveList = reserveService.reserveList();


            return ResponseEntity.ok().body(reserveList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error. en Findall");
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReserveById( @PathVariable("id") String id ) {
        try {
            Reserve reserve = reserveService.getReserveById(id);

            return ResponseEntity.ok().body(reserve);
        } catch (Exception e) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("{\"error\":\"Error. En getReserveById\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete( @PathVariable("id") String id ) throws Exception {
        reserveService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
