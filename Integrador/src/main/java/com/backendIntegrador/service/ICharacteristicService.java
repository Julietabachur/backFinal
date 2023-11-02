package com.backendIntegrador.service;

import com.backendIntegrador.model.Characteristic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface ICharacteristicService {
    Characteristic save( Characteristic characteristic ) throws Exception;

    Page<Characteristic> findAll( Pageable pageable ) throws Exception;

    Characteristic getCharById( String id ) throws Exception;

    boolean delete( String id ) throws Exception;

    @Transactional
    Characteristic getCharByCharName( String charName );

    boolean checkCharName( String charName );

    Characteristic update( Characteristic characteristic ) throws Exception;
}
