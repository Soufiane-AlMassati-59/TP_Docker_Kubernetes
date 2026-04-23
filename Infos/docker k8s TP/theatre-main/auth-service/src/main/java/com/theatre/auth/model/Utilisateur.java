package com.theatre.auth.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité Utilisateur (lecture seule pour l'auth-service)
 * Utilisée uniquement pour vérifier les credentials
 */
@Entity
@Table(name = "utilisateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String prenom;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    @Column(nullable = false)
    private boolean actif = true;
}
