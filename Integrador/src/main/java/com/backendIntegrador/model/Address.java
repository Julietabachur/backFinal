package com.backendIntegrador.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "address")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    @Id
    private String id;
    private String street;
    private int number;
    private String city;
    private String country;
    private int postalCode;
}
