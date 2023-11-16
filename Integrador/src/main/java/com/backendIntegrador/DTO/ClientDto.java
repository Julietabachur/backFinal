package com.backendIntegrador.DTO;

import com.backendIntegrador.model.Address;
import com.backendIntegrador.model.Reserve;
import com.backendIntegrador.model.Role;
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
public class ClientDto {
    private String id;
    private String firstName;
    private String lastName;
    private String clientName;
    private Set<Role> roles;
    private boolean isVerified;
    private String email;
    private String cel;
    private Address address;
    private List<String> reserveIds;
    private List<String> favorites;

}
