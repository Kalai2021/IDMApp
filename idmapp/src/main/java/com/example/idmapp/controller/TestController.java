package com.example.idmapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("/protected")
    public String protectedEndpoint() {
        return "This is a protected endpoint!";
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint!";
    }
} 