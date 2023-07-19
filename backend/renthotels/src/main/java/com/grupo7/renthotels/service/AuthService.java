package com.grupo7.renthotels.service;

import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.model.User;
import com.grupo7.renthotels.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            try {
                throw new NotFoundException("User not found");
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        List<String> functions = new ArrayList<>();

        functions.add(user.get().getFunction().getName());

        authorities = functions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return user.get();
    }
}
