package com.backendIntegrador.DTO;

import com.backendIntegrador.model.Address;
import com.backendIntegrador.model.Role;
import com.backendIntegrador.model.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Data       //this give us getters & setters
@SuperBuilder
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String productName;
    private String thumbnail;
    private Integer Amount;
    private String size;
    private double price;
}
