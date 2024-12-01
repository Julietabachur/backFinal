package com.backendIntegrador.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data       //this give us getters & setters
@SuperBuilder
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportePpCDto {

    private String name;
    private Integer amount;
}
