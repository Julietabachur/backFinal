package com.backendIntegrador.repository;

import com.backendIntegrador.model.Car;
import com.backendIntegrador.model.Category;
import com.backendIntegrador.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {

    Car findByIdUser(String idUser);
}
