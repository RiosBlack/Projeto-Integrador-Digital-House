package com.grupo7.renthotels.service;

import com.grupo7.renthotels.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {
    @Value("${api.jwt.expiration}")
    private String expiration;

    @Value("${api.jwt.secret}")
    private String secret;

    public String generateToken(Authentication authentication) {
        User loggedUser = (User) authentication.getPrincipal();
        Date today = new Date();
        Date expirationDate = new Date(System.currentTimeMillis() + Integer.parseInt(expiration));

        return Jwts.builder()
                .setIssuer("DigitalBooking")
                .setSubject(loggedUser.getEmail())
                .setIssuedAt(today)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isValidToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
            Date expirationDate = claims.getExpiration();
            Date currentDate = new Date();

            if (expirationDate.before(currentDate)) {
                return false;
            }

            long expirationTimeInMillis = expirationDate.getTime();
            long currentTimeInMillis = currentDate.getTime();
            long allowedTimeInMillis = 2 * 60 * 60 * 1000;

            return (expirationTimeInMillis - currentTimeInMillis) <= allowedTimeInMillis;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUserEmail(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
