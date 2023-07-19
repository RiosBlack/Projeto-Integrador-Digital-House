package com.grupo7.renthotels.service;

import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.model.Function;
import com.grupo7.renthotels.model.User;
import com.grupo7.renthotels.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        Function function = new Function();
        function.setIdFunction(1L);
        function.setName("Admin");

        user = new User();
        user.setUserSku(1234567L);
        user.setEmail("user@test.com");
        user.setPassword("usertestpass");
        user.setFunction(function);
    }

    @Test
    void shouldLoadUserByUsername() throws NotFoundException {
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = authService.loadUserByUsername("user@test.com");

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("Admin", authorities.iterator().next().getAuthority());

    }

    @Test
    void shoulNotLoadWithInexistingUser() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.loadUserByUsername("nonexistent@example.com");
        });

    }
}
