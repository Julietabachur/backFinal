package com.backendIntegrador.service.impl;

import com.backendIntegrador.DTO.ProductDto;
import com.backendIntegrador.model.Car;
import com.backendIntegrador.model.Client;
import com.backendIntegrador.repository.CarRepository;
import com.backendIntegrador.service.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService implements ICarService {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private ClientService clientService; //metodos del clientservice para ver si existe el usuario

   // @Override
    //public Car save(Car car) throws Exception {
      //  Client existingClient = clientService.getClientById(car.getIdUser());
        //if (existingClient == null){
          //  throw new Exception();
        //}
        //Car existingCar = carRepository.findByIdUser(car.getIdUser()); // chequea que existe un carrito con ese usuario
        //if (existingCar == null){
          //  throw new Exception();
       //}

        //Car savedCar = carRepository.save(car);
        //return savedCar;
    //}
        @Override
        public Car save(Car car) throws Exception {
            Client existingClient = clientService.getClientById(car.getIdUser());
            if (existingClient == null){
                throw new Exception();
            }
            Car existingCar = carRepository.findByIdUser(car.getIdUser()); // chequea que existe un carrito con ese usuario
            if (existingCar != null){
                throw new Exception();
            }
            double total = 0.0;

            for(ProductDto product : car.getProducts()){
                total += product.getPrice() * product.getAmount();
            }

            car.setTotalPrice(total);

            Car savedCar = carRepository.save(car);
            return savedCar;
        }


    @Override
    public Car findByIdUser(String idUser) throws Exception {
        return carRepository.findByIdUser(idUser);
    }

    @Override
    public List<Car> findAll() throws Exception {
        return carRepository.findAll();
    }

    @Override
    public Car update(Car car) throws Exception {
        Client existingClient = clientService.getClientById(car.getIdUser()); // chequea q existe el usuario
        if (existingClient == null){
            throw new Exception();
        }
        Car existingCar = carRepository.findByIdUser(car.getIdUser()); // chequea que existe un carrito con ese usuario
        if (existingCar == null){
            throw new Exception();
        }
        Car filledCar = carRepository.findById(car.getId()).orElse(null); // Chequea si existe carrito con productos para actualizar
        if (filledCar == null){
            throw new Exception();
        }
        double total = 0.0;

        for(ProductDto product : car.getProducts()){
            total += product.getPrice() * product.getAmount();
        }

        car.setTotalPrice(total);

        Car savedCar = carRepository.save(car);
        return savedCar;
    }

    @Override
    public void delete(String id) throws Exception {
        Car existingCar = carRepository.findById(id).orElse(null);
        if (existingCar != null){
            carRepository.deleteById(id);
        }
    }
}
