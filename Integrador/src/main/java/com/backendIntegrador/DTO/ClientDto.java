package com.backendIntegrador.DTO;

import com.backendIntegrador.model.Address;
import com.backendIntegrador.model.Role;
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
public class ClientDto {
    private String id;
    private String clientName;
    private Role role = Role.USER;
    private String email;
    private String cel;

}
