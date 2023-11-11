package com.backendIntegrador.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data //getters y setters
@Builder
@NoArgsConstructor //constructor vacio
@AllArgsConstructor //constructor con todos los atributos
@Document(collection = "product") // nombre de la ubicacion de los datos en la BD
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    @Id
    private String id;
    private Long productId;
    private String productName;
    private String thumbnail;
    private List<String> gallery;
    private String Detail;
    private List<Characteristic> features;
    private String category;
    private List<String> reserveIds;

    public List<String> getReserveIds() {
        if (reserveIds == null) {
            reserveIds = new ArrayList<>();
        }
        return reserveIds;
    }

    public void setReserveIds(List<String> reserveIds) {
        this.reserveIds = reserveIds;
    }

}
