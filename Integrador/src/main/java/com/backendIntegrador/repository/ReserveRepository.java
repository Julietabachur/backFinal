package com.backendIntegrador.repository;

import com.backendIntegrador.model.Reserve;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReserveRepository extends MongoRepository<Reserve, String> {
    List<Reserve> findByProductId( String productId );

    List<Reserve> findByIdIn( List<String> reserveIds );

    List<Reserve> findByProductIdIn( List<String> idList );
}
