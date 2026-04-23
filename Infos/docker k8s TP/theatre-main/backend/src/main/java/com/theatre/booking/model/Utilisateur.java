package com.theatre.booking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

/**
 * Entité Utilisateur
 * Pattern: Entity (JPA)
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
    
    @Column(nullable = false, length = 100)
    private String nom;
    
    @Column(nullable = false, length = 100)
    private String prenom;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false, name = "mot_de_passe")
    private String motDePasse;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    
    @Column(name = "date_inscription")
    private LocalDateTime dateInscription = LocalDateTime.now();
    
    @Column(nullable = false)
    private Boolean actif = true;
    
    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
    
    /**
     * Vérifie si l'utilisateur est actif
     */
    public Boolean isActif() {
        return this.actif;
    }
    
    /**
     * Vérifie si l'utilisateur est administrateur
     */
    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
    
    /**
     * Retourne le nom complet
     */
    public String getNomComplet() {
        return this.prenom + " " + this.nom;
    }
}
