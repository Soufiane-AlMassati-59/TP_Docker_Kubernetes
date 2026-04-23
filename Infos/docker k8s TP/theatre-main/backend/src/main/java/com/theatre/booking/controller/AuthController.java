package com.theatre.booking.controller;

import com.theatre.booking.dto.request.LoginRequest;
import com.theatre.booking.dto.request.RegisterRequest;
import com.theatre.booking.dto.response.AuthResponse;
import com.theatre.booking.service.UtilisateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour l'authentification (Booking Service)
 * Pattern: MVC Controller
 * 
 * Responsabilités:
 * - /auth/register : Création de compte
 * - /auth/login : Authentification
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final UtilisateurService utilisateurService;
    
    /**
     * Inscription d'un nouvel utilisateur
     * Géré localement dans le booking-service
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = utilisateurService.register(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Connexion d'un utilisateur
     * Géré localement dans le booking-service
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = utilisateurService.login(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Vérification du token JWT (non implémenté)
     */
    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok("Token validation not implemented in booking-service");
    }
}
