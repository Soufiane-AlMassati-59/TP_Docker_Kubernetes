package com.theatre.booking.dto.response;

import com.theatre.booking.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la réponse d'authentification
 * Pattern: Data Transfer Object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private Role role;
    
    public AuthResponse(String token, Long id, String email, String nom, String prenom, Role role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
    }
}
