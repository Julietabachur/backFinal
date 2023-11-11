package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Client;
import com.backendIntegrador.model.Product;
import com.backendIntegrador.model.Reserve;
import com.backendIntegrador.repository.ReserveRepository;
import com.backendIntegrador.service.IReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReserveService implements IReserveService {

    @Autowired
    private final ReserveRepository reserveRepository;
    @Autowired
    private final ProductService productService;

    private ClientService clientService;

    @Autowired
    public ReserveService( ReserveRepository reserveRepository, ProductService productService, ClientService clientService ) {
        this.reserveRepository = reserveRepository;
        this.productService = productService;
        this.clientService = clientService;
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Reserve save(Reserve reserve) throws Exception {
        try {
            // Verifica si el producto está disponible en la fecha proporcionada
            if (!isProductAvailable(reserve.getProductId(), reserve.getStartDate(), reserve.getEndDate())) {
                throw new Exception("El producto no está disponible en la fecha proporcionada.");
            }

            // Guarda la reserva y obtiene el ID generado
            Reserve reserveMade = reserveRepository.save(reserve);

            // Actualiza el cliente con el nuevo ID de reserva
            Client client = clientService.getClientById(reserve.getClientId());
            client.getReserveIds().add(reserveMade.getId());
            clientService.update(client);

            // Actualiza el producto con el nuevo ID de reserva
            Product product = productService.getProductById(reserve.getProductId());
            product.getReserveIds().add(reserveMade.getId());
            productService.update(product);

            return reserveMade;
        } catch (Exception e) {
            throw new Exception("Error al guardar la reserva: " + e.getMessage());
        }
    }

    // Método para verificar si el producto está disponible en la fecha proporcionada
    private boolean isProductAvailable( String productId, LocalDate startDate, LocalDate endDate) {
        // Implementa la lógica de verificación según tu modelo de datos y la estructura de las reservas
        // Puedes consultar las reservas existentes para el producto y verificar si hay superposición con las fechas proporcionadas

        // Ejemplo simplificado: Consultar las reservas para el producto y verificar la disponibilidad
        List<Reserve> existingReservations = reserveRepository.findByProductId(productId);
        for (Reserve existingReserve : existingReservations) {
            if (isDateRangeOverlap(existingReserve.getStartDate(), existingReserve.getEndDate(), startDate, endDate)) {
                return false; // Hay superposición de fechas, el producto no está disponible
            }
        }

        return true; // El producto está disponible
    }

    // Método para verificar la superposición de rangos de fechas
    private boolean isDateRangeOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }


    @Override
    @Transactional
    public List<Reserve> reserveList() throws Exception {
        try {
            return reserveRepository.findAll();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Reserve getReserveById( String id ) throws Exception {
        try {
            return reserveRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public boolean delete( String id ) throws Exception {
        try {
            if (reserveRepository.existsById(id)) {
                reserveRepository.deleteById(id);
                return true;
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return false;

    }
}
