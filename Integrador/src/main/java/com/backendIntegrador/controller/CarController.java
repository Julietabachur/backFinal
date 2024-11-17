package com.backendIntegrador.controller;

import com.backendIntegrador.model.Car;
import com.backendIntegrador.service.impl.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/private/car")
@RequiredArgsConstructor
public class CarController {

    @Autowired
    private final CarService carService;


    @PostMapping("")
    public Car save(@RequestBody Car car) throws Exception {
        Car savedCar = carService.save(car);
        return savedCar;
    };

    @GetMapping("")
    public List<Car> findAll() throws Exception{
        return carService.findAll();
    }

    @GetMapping("/{idUser}")
    public Car findById(@PathVariable String idUser) throws Exception{
        return carService.findByIdUser(idUser);
    }

    @PutMapping("")
    public Car update(@RequestBody Car car) throws Exception{
        Car updatedCar = carService.update(car);
        return updatedCar;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) throws Exception{
        carService.delete(id);
    }
}
