package com.theatre.booking.service;

import com.theatre.booking.dto.request.LoginRequest;
import com.theatre.booking.dto.request.RegisterRequest;
import com.theatre.booking.dto.response.AuthResponse;
import com.theatre.booking.model.Role;
import com.theatre.booking.model.Utilisateur;
import com.theatre.booking.repository.UtilisateurRepository;
import com.theatre.booking.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service pour la gestion des utilisateurs
 * Pattern: Service Layer Pattern
 * 
 * Responsabilité : Création de compte ET authentification
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurService {
    
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    /**
     * Inscription d'un nouvel utilisateur
     * Crée un utilisateur et génère immédiatement un token JWT
     */
    public AuthResponse register(RegisterRequest request) {
        // Vérifier si l'email existe déjà
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }
        
        Utilisateur utilisateur = Utilisateur.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .role(Role.USER)
                .actif(true)
                .build();
        
        utilisateur = utilisateurRepository.save(utilisateur);
        
        // Générer un vrai token JWT
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
    
    // La méthode login() a été déplacée dans le microservice auth-service
    // Le booking-service ne gère plus l'authentification, seulement la création de compte
    
    public Utilisateur creerUtilisateur(RegisterRequest request) {
        // Vérifier si l'email existe déjà
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }
        
        Utilisateur utilisateur = Utilisateur.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .role(Role.USER)
                .actif(true)
                .build();
        
        return utilisateurRepository.save(utilisateur);
    }
    
    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
    
    public Utilisateur findById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
