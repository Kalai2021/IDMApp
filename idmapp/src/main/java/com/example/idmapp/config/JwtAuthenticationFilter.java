package com.example.idmapp.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final com.auth0.jwt.interfaces.JWTVerifier jwtVerifier;

    public JwtAuthenticationFilter(@Qualifier("idmappJwtVerifier") com.auth0.jwt.interfaces.JWTVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
        logger.debug("JwtAuthenticationFilter initialized with verifier: {}", jwtVerifier);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        logger.debug("Processing request to: {} with auth header: {}", request.getRequestURI(), authHeader);
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No valid Authorization header found for path: {}, continuing chain", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            logger.debug("Attempting to verify token for path: {}", request.getRequestURI());
            logger.debug("Token: {}", token);
            
            DecodedJWT jwt = jwtVerifier.verify(token);
            logger.debug("Token verified successfully for subject: {} and path: {}", jwt.getSubject(), request.getRequestURI());
            logger.debug("Token claims: {}", jwt.getClaims());
            logger.debug("Token issuer: {}", jwt.getIssuer());
            logger.debug("Token audience: {}", jwt.getAudience());

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            var permissionsClaim = jwt.getClaim("permissions");
            if (permissionsClaim != null && !permissionsClaim.isNull()) {
                List<String> permissions = permissionsClaim.asList(String.class);
                if (permissions != null) {
                    authorities = permissions.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                }
            }
            logger.debug("Extracted authorities: {} for path: {}", authorities, request.getRequestURI());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                jwt.getSubject(),
                null,
                authorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Authentication set in SecurityContext for path: {}", request.getRequestURI());
        } catch (Exception e) {
            logger.error("Failed to process JWT token for path: {}", request.getRequestURI(), e);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public String toString() {
        return "JwtAuthenticationFilter{" +
            "jwtVerifier=" + jwtVerifier +
            '}';
    }
} 