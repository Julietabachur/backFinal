package com.backendIntegrador.service;

import com.backendIntegrador.model.Reserve;

import java.util.List;

public interface IReserveService {
    Reserve save( Reserve reserve ) throws Exception;

    List<Reserve> reserveList() throws Exception;

    Reserve getReserveById( String id ) throws Exception;

    boolean delete( String id ) throws Exception;

    List<Reserve> getReserveByIdIn( List<String> idList );
}
