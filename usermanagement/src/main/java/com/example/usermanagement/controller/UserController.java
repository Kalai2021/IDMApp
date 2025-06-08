package com.example.usermanagement.controller;

import java.util.UUID;
import com.example.usermanagement.dto.*;
import com.example.usermanagement.model.User;
import com.example.usermanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        System.out.println("Getting users");
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(name = "id") UUID id) {
        System.out.println("Getting user by ID: " + id);
        return userService.getUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest request) {
        return ResponseEntity.status(201).body(userService.createUser(request));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(name = "id") UUID id, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return userService.authenticate(request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }
}