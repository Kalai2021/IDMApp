package com.example.usermanagement.service;

import com.example.usermanagement.dto.*;
import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    @Value("${auth0.domain}")
    private String auth0Domain;

    @Value("${auth0.client-id}")
    private String clientId;

    @Value("${auth0.client-secret}")
    private String clientSecret;

    @Value("${auth0.audience}")
    private String audience;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = new RestTemplate();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(UUID id) {
        return userRepository.findById(id);
    }

    public User createUser(UserCreateRequest request) {
        logger.info("Creating user with request: name={}, email={}, firstName={}, lastName={}", 
            request.getName(), request.getEmail(), request.getFirstName(), request.getLastName());
        User user = new User();
        user.setName(request.getName());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setId(UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        logger.info("Created user object: name={}, email={}, firstName={}, lastName={}", 
            user.getName(), user.getEmail(), user.getFirstName(), user.getLastName());
        return userRepository.save(user);
    }

    public Optional<User> updateUser(UUID id, UserUpdateRequest request) {
        logger.info("UserUpdateRequest payload: name={}, email={}", request.getName(), request.getEmail());
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            return Optional.empty();
        }
        
        User user = existingUserOpt.get();
        int rows = userRepository.update(user);
        if ( rows == 0 ) {
            return Optional.empty();
        } 

        return Optional.of(user);
        
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public Optional<AuthResponse> authenticate(LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(user -> {
                    // Get token from Auth0
                    String tokenUrl = "https://" + auth0Domain + "/oauth/token";
                    
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
                    body.add("grant_type", "client_credentials");
                    body.add("client_id", clientId);
                    body.add("client_secret", clientSecret);
                    body.add("audience", audience);

                    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

                    Auth0TokenResponse response = restTemplate.postForObject(
                        tokenUrl,
                        requestEntity,
                        Auth0TokenResponse.class
                    );

                    if (response != null && response.getAccessToken() != null) {
                        return new AuthResponse(response.getAccessToken(), response.getExpiresIn());
                    }
                    return null;
                });
    }
}