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
@Document(collection = "client") // nombre de la ubicacion de los datos en la BD
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Client {

    @Id
    private String id;
    private String clientName;
    private String password;
    private Role role = Role.USER;
    private String email;
    private String cel;
    private Address address;
}
