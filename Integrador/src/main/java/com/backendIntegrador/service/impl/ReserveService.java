package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Reserve;
import com.backendIntegrador.repository.ReserveRepository;
import com.backendIntegrador.service.IReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReserveService implements IReserveService {

    @Autowired
    private final ReserveRepository reserveRepository;

    @Autowired
    public ReserveService( ReserveRepository reserveRepository ) {
        this.reserveRepository = reserveRepository;
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Reserve save( Reserve reserve ) throws Exception {
        try {
            reserveRepository.save(reserve);
            return reserve;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
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
