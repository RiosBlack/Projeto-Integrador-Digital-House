package com.grupo7.renthotels.security;

import com.grupo7.renthotels.model.Function;
import com.grupo7.renthotels.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

@Configuration
public class SpringSecurityWebAuxTestConfig {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        Function function = new Function(1L, 1L, "Admin");
        User basicUser = new User("User", "Test", "user@test.com", "testuserpass", function, List.of());

        return new InMemoryUserDetailsManager(List.of(basicUser));
    }
}
