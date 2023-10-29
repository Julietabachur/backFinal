package com.backendIntegrador.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data //getters y setters
@Builder
@NoArgsConstructor //constructor vacio
@AllArgsConstructor //constructor con todos los atributos
@Document(collection = "client") // nombre de la ubicación de los datos en la BD de mongo
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Client implements UserDetails {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String clientName;
    private String password;
    private Role role = Role.USER;
    private String email;
    private String cel;
    private Address address;
    private List<Reserve> reserves;

    public Client(String email, String username) {
        this.email = email;
        this.clientName = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return clientName;
    }


    public String getEmail() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
