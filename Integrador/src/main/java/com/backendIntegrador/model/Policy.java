package com.backendIntegrador.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Document(collection = "policy") // nombre de la ubicacion de los datos en la BD
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Policy {
    @Id
    private String id;

    @NotBlank(message = "El nombre de la politica no puede estar en blanco.")
    @Size(min = 3, max = 30, message = "El nombre de politica debe tener entre 3 y 30 caracteres.")
    private String policyName;
    private String description;
}