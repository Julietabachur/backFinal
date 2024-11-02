package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.ProductId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ProductIdService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Long getNextSequence(String product) {
        Query query = new Query(Criteria.where("product").is(product));
        Update update = new Update().inc("sequence", 1);
        ProductId productId = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true).upsert(true), ProductId.class);
        return productId.getSequence();
    }
}
