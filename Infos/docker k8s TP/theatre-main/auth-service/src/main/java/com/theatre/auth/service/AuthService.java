package com.theatre.auth.service;

import com.theatre.auth.dto.AuthResponse;
import com.theatre.auth.dto.LoginRequest;
import com.theatre.auth.model.Utilisateur;
import com.theatre.auth.repository.UtilisateurRepository;
import com.theatre.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service d'authentification (Microservice)
 * Responsabilité : Login uniquement
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    /**
     * Connexion d'un utilisateur
     * Vérifie les credentials et génère un token JWT
     */
    public AuthResponse login(LoginRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Email ou mot de passe incorrect"));
        
        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }
        
        if (!utilisateur.isActif()) {
            throw new RuntimeException("Compte désactivé");
        }
        
        // Générer un token JWT
        String token = jwtUtil.generateToken(
                utilisateur.getId(), 
                utilisateur.getEmail(), 
                utilisateur.getRole().name()
        );
        
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(utilisateur.getId())
                .email(utilisateur.getEmail())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .role(utilisateur.getRole())
                .build();
    }
    
    /**
     * Vérifie la validité d'un token JWT
     */
    public boolean verifyToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
