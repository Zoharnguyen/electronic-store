package com.altech.electronic_store.controller;

import com.altech.electronic_store.dto.AuthRequestDTO;
import com.altech.electronic_store.dto.RegisterRequestDTO;
import com.altech.electronic_store.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/client-api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO request) {
        log.debug("Request body: {}", request);
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody AuthRequestDTO request) {
        log.info("Received login request for user: {}", request.getUsername());
        String token = authService.login(request);
        return ResponseEntity.ok(Map.of("token", token));
    }
} 