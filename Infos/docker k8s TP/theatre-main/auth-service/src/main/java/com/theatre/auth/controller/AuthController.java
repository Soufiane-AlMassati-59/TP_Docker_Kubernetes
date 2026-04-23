package com.theatre.auth.controller;

import com.theatre.auth.dto.AuthResponse;
import com.theatre.auth.dto.LoginRequest;
import com.theatre.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour l'authentification (Microservice)
 * Endpoints : /auth/login et /auth/verify uniquement
 * La création de compte est gérée par le booking-service
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * Connexion d'un utilisateur
     * Endpoint: POST /auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Vérification du token JWT
     * Endpoint: GET /auth/verify
     */
    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            boolean isValid = authService.verifyToken(token);
            if (isValid) {
                return ResponseEntity.ok("Token valide");
            }
        }
        return ResponseEntity.status(401).body("Token invalide");
    }
    
    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth Service is running");
    }
}
