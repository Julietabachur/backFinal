package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Client;
import com.backendIntegrador.model.Role;
import com.backendIntegrador.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = null;
        Client client = clientRepository.findByEmail(username);

        if (client != null) {
            Set<GrantedAuthority> grantList = new HashSet<>();

            for (Role role : client.getRoles()) {
                grantList.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
            }

            user = new User(username, client.getPassword(), grantList);
        }

        return user;
    }
}
