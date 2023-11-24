package com.backendIntegrador.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

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
    private Set<Role> roles;
    private String isVerified; // estaba private y asignaba el valor false
    private String email;
    private String cel;
    private Address address;
    private List<String> reserveIds;
    private List<String> favorites;

    public Client( String email, String username ) {
        this.email = email;
        this.clientName = username;
    }


    public List<String> getReserveIds() {
        if (reserveIds == null) {
            reserveIds = new ArrayList<>();
        }
        return reserveIds;
    }

    public void setReserveIds(List<String> reserveIds) {
        this.reserveIds = reserveIds;
    }

    public List<String> getFavorites() {
        if (favorites == null) {
            favorites = new ArrayList<>();
        }
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = Arrays.stream(Role.values())
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
        return authorities;
    }


    @Override
    public String getUsername() {
        return clientName;
    }


    public String getEmail() {
        return email;
    }
// No requiere @Override ?

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
