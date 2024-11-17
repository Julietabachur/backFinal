package com.backendIntegrador.service;

import com.backendIntegrador.model.Car;

import java.util.List;

public interface ICarService {
    Car save(Car car) throws Exception;

    //Car findById(String Id) throws Exception;

    Car findByIdUser(String idUser) throws Exception;

    List<Car> findAll() throws Exception;

    Car update(Car car) throws Exception;
    void delete(String Id) throws Exception;
}
