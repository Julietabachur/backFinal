package com.backendIntegrador.repository;

import com.backendIntegrador.model.Reserve;
import com.backendIntegrador.model.Sale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SaleRepository extends MongoRepository<Sale, String> {



}
