package com.backendIntegrador.service;

import com.backendIntegrador.model.Category;

public interface ICategoryService {
    Category save( Category category );

    void delete( String id );
}
