package com.backendIntegrador.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data //getters y setters
@Builder
@NoArgsConstructor //constructor vacio
@AllArgsConstructor //constructor con todos los atributos
@Document(collection = "productId")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductId {
    @Id
    private String id;
    private String product; // Nombre de la colección para la que se genera la secuencia
    private Long sequence;

}
