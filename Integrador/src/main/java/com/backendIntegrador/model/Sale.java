package com.backendIntegrador.model;


import com.backendIntegrador.DTO.ProductDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data //getters y setters
@Builder
@NoArgsConstructor //constructor vacio
@AllArgsConstructor //constructor con todos los atributos
@Document(collection = "sale") // nombre de la ubicacion de los datos en la BD
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sale {
    @Id
    private String id;
    private List<ProductDto> productList;
    private String idUser;
    private String entrega;
    private String domicilio;
    private String medioDePago;
    private double TotalPrice;
    private LocalDate saleDate;


}
