package com.backendIntegrador.model;

import com.backendIntegrador.DTO.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data //getters y setters
@Builder
@NoArgsConstructor //constructor vacio
@AllArgsConstructor //constructor con todos los atributos
@Document(collection = "car")
public class Car {

    @Id
    private String id;
    private String idUser;
    private List<ProductDto> products;
    private double TotalPrice;

}
