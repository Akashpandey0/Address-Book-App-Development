package com.project.AddressBookAppDevelopment.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtility {

    private static final String SECRET_KEY = "YourSecretKeyForJWTGenerationMustBeAtLeast32CharactersLong";

    public String generateToken(String email,String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 1)) // 1hr
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public String extractRole(String token) {
        return (String) Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    public boolean validateToken(String token, String userEmail, String expectedRole) {
        String tokenRole = extractRole(token);
        return extractEmail(token).equals(userEmail)
                && tokenRole.equals(expectedRole)
                && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}