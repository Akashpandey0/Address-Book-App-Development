package com.project.AddressBookAppDevelopment.filter;

import com.project.AddressBookAppDevelopment.exception.UnauthorizedAccessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.Key;
import java.util.Collections;

@Component
public class JwtFilter extends HttpFilter {

    private static final String SECRET_KEY = "YourSecretKeyForJWTGenerationMustBeAtLeast32CharactersLong"; // 256-bit key required

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println(" JwtFilter Triggered ");
        if (request.getRequestURI().startsWith("/auth/") ||
                request.getRequestURI().startsWith("/error") ||
                request.getRequestURI().startsWith("/swagger-ui/") ||
                request.getRequestURI().startsWith("/v3/api-docs/")) {
            System.out.println("Bypassing filter for: " + request.getRequestURI());
            chain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().startsWith("/auth/") || request.getRequestURI().startsWith("/error")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("Auth Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError
                    (HttpServletResponse.SC_UNAUTHORIZED, "Access Denied: Valid token required to access this API.");
            return;
        }

        String token = authHeader.substring(7);
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String role = claims.get("role", String.class);
            String requestURI = request.getRequestURI();
            System.out.println("Decoded Claims: " + claims);
            System.out.println("Role: " + role);
            System.out.println("Requested URI: " + requestURI);

            if (role == null ||
                    (requestURI.startsWith("/addressbook") && !role.equalsIgnoreCase("USER") && !role.equalsIgnoreCase("ADMIN"))
            ) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Access Denied: Insufficient permissions.");
                return;
            }
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
//            UsernamePasswordAuthenticationToken authToken =
//                    new UsernamePasswordAuthenticationToken(null, null, Collections.singletonList(authority));
//
//            SecurityContextHolder.getContext().setAuthentication(authToken);
            request.setAttribute("claims", claims);
            chain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied: Invalid token provided.");
        }
    }
}