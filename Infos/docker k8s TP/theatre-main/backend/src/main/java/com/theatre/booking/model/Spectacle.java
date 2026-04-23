package com.theatre.booking.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.*;

/**
 * Entité Spectacle
 * Pattern: Entity (JPA)
 */
@Entity
@Table(name = "spectacles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Spectacle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_spectacle")
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String titre;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, name = "date_spectacle")
    private LocalDate dateSpectacle;
    
    @Column(nullable = false, name = "heure_debut")
    private LocalTime heureDebut;
    
    @Column(nullable = false, name = "prix_unitaire", precision = 10, scale = 2)
    private BigDecimal prixUnitaire;
    
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    @Column(nullable = false, name = "places_totales")
    private Integer placesTotales;
    
    @Column(nullable = false, name = "places_disponibles")
    private Integer placesDisponibles;
    
    @Column(length = 50)
    private String categorie = "Théâtre";
    
    @Column(nullable = false)
    private Boolean actif = true;
    
    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();
    
    @OneToMany(mappedBy = "spectacle", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
    
    /**
     * Vérifie si le nombre de places demandé est disponible
     */
    public boolean verifierDisponibilite(int quantite) {
        return this.placesDisponibles >= quantite && this.actif;
    }
    
    /**
     * Réserve un nombre de places
     */
    public void reserverPlaces(int quantite) throws Exception {
        if (!verifierDisponibilite(quantite)) {
            throw new Exception("Places insuffisantes");
        }
        this.placesDisponibles -= quantite;
    }
    
    /**
     * Libère un nombre de places (annulation)
     */
    public void libererPlaces(int quantite) {
        this.placesDisponibles = Math.min(
            this.placesDisponibles + quantite, 
            this.placesTotales
        );
    }
    
    /**
     * Calcule le taux de remplissage en pourcentage
     */
    public double getTauxRemplissage() {
        if (placesTotales == 0) return 0;
        return ((placesTotales - placesDisponibles) * 100.0) / placesTotales;
    }
    
    /**
     * Calcule le revenu potentiel du spectacle
     */
    public BigDecimal getRevenuPotentiel() {
        int billetVendus = placesTotales - placesDisponibles;
        return prixUnitaire.multiply(BigDecimal.valueOf(billetVendus));
    }
}
