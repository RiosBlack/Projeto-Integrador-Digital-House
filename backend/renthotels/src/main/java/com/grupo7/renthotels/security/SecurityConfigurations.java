package com.grupo7.renthotels.security;

import com.amazonaws.services.xray.model.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    AuthenticateTokenFilter authenticateTokenFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     http
//             .cors().configurationSource(request -> {
//                 CorsConfiguration cors = new CorsConfiguration();
//                 cors.applyPermitDefaultValues();
//                 cors.addExposedHeader("Authorization");
//                 return cors;
//             })
//             .and()
//             .csrf().disable()
//             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .authorizeHttpRequests(requests -> requests
//                     .requestMatchers(
//                             HttpMethod.GET,
//                             "/api/products",
//                             "/api/products/**",
//                             "/api/categories",
//                             "/api/categories/**",
//                             "/api/cities",
//                             "/api/cities/**",
//                             "/api/bookings",
//                             "/api/bookings/**",
//                             "/api/s3/list"
//                     ).permitAll()
//                     .requestMatchers(HttpMethod.POST,
//                             "/api/bookings",
//                             "api/bookings/**",
//                             "/api/s3/upload"
//                             )
//                     .hasAnyAuthority("Admin", "User")
//                     .requestMatchers(
//                             HttpMethod.POST,
//                             "/api/functions",
//                             "/api/products")
//                     .hasAuthority("Admin")
//                     .requestMatchers(
//                             "/swagger-ui/**",
//                             "/v3/api-docs/**")
//                     .permitAll()
//                     .requestMatchers(HttpMethod.POST,
//                             "/api/users/login",
//                             "/api/users",
//                             "/api/users/**").permitAll()
//                     .anyRequest().authenticated())
//             .addFilterBefore(authenticateTokenFilter, UsernamePasswordAuthenticationFilter.class);
//             return http.build();

             //Abrindo para todas as requisições
             .authorizeHttpRequests((authz) -> authz
                     .anyRequest().permitAll())
             .httpBasic(withDefaults())
             .cors().configurationSource(request -> {
                 CorsConfiguration cors = new CorsConfiguration();
                 cors.applyPermitDefaultValues();
                 cors.addExposedHeader("Authorization"); 
                 return cors;
             })
             .and()
             .csrf().disable();
        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
