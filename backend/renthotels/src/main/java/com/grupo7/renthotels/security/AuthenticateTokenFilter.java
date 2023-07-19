package com.grupo7.renthotels.security;

import com.grupo7.renthotels.model.User;
import com.grupo7.renthotels.repository.UserRepository;
import com.grupo7.renthotels.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthenticateTokenFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenRecovery(request);

        boolean valid = tokenService.isValidToken(token);

        if(valid) {
            userAuthenticate(token);
        }

        filterChain.doFilter(request, response);

    }

    private void userAuthenticate(String token) {
        String userEmail = tokenService.getUserEmail(token);
        Optional<User> user = userRepository.findByEmail(userEmail);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.get().getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String tokenRecovery(HttpServletRequest request) {
        String getToken = request.getHeader("Authorization");
        if(getToken == null || getToken.isEmpty() || !getToken.startsWith("Bearer ")) {
            return null;
        }

        return getToken.substring(7,getToken.length());
    }
}
