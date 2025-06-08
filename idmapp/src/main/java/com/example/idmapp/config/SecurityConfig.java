package com.example.idmapp.config;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.client-id}")
    private String clientId;

    @Value("${auth0.client-secret}")
    private String clientSecret;

    @Bean(name = "securityFilterChain")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.debug("=== Security Configuration ===");
        logger.debug("Auth0 Domain: {}", domain);
        logger.debug("Auth0 Audience: {}", audience);
        logger.debug("Auth0 Client ID: {}", clientId);
        logger.debug("Auth0 Client Secret: {}", clientSecret != null ? "****" : "null");
        logger.debug("============================");
        
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtVerifier());
        logger.debug("Created JWT filter: {}", jwtFilter);
        
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/v1/test/public").permitAll()
                .requestMatchers("/api/v1/test/protected").authenticated()
                .requestMatchers("/api/v1/**").authenticated()
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource())
            )
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }

    @Bean(name = "jwtVerifier")
    public com.auth0.jwt.interfaces.JWTVerifier jwtVerifier() throws Exception {
        String jwksUrl = "https://" + domain + "/.well-known/jwks.json";
        logger.debug("Creating JWT verifier with JWKS URL: {}", jwksUrl);
        
        JwkProvider jwkProvider = new UrlJwkProvider(new URL(jwksUrl));

        RSAKeyProvider keyProvider = new RSAKeyProvider() {
            @Override
            public RSAPublicKey getPublicKeyById(String kid) {
                try {
                    logger.debug("Getting public key for kid: {}", kid);
                    Jwk jwk = jwkProvider.get(kid);
                    return (RSAPublicKey) jwk.getPublicKey();
                } catch (Exception e) {
                    logger.error("Failed to get public key for kid: {}", kid, e);
                    throw new RuntimeException("Failed to get public key", e);
                }
            }

            @Override
            public RSAPrivateKey getPrivateKey() {
                return null;
            }

            @Override
            public String getPrivateKeyId() {
                return null;
            }
        };

        String issuer = "https://" + domain + "/";
        logger.debug("Creating JWT verifier with issuer: {} and audience: {}", issuer, audience);
        
        // Create a custom verifier that checks if the audience starts with our expected value
        com.auth0.jwt.interfaces.JWTVerifier baseVerifier = JWT.require(Algorithm.RSA256(keyProvider))
            .withIssuer(issuer)
            .build();
            
        // Create a custom verifier that wraps the base verifier
        return new com.auth0.jwt.interfaces.JWTVerifier() {
            private final com.auth0.jwt.interfaces.JWTVerifier delegate = baseVerifier;

            @Override
            public DecodedJWT verify(String token) throws com.auth0.jwt.exceptions.JWTVerificationException {
                DecodedJWT jwt = delegate.verify(token);
                List<String> tokenAudiences = jwt.getAudience();
                
                // Check if any of the token's audiences start with our expected audience
                boolean validAudience = tokenAudiences.stream()
                    .anyMatch(aud -> aud.startsWith(audience));
                
                if (!validAudience) {
                    throw new com.auth0.jwt.exceptions.JWTVerificationException(
                        "The Claim 'aud' value doesn't start with the required audience: " + audience);
                }
                
                return jwt;
            }

            @Override
            public DecodedJWT verify(DecodedJWT jwt) throws com.auth0.jwt.exceptions.JWTVerificationException {
                return verify(jwt.getToken());
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 