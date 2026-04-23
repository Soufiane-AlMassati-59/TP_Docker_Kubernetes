package com.theatre.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO pour la réponse contenant les détails d'un spectacle
 * Pattern: Data Transfer Object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpectacleResponse {
    
    private Long id;
    private String titre;
    private String description;
    private LocalDate dateSpectacle;
    private LocalTime heureDebut;
    private BigDecimal prixUnitaire;
    private String imageUrl;
    private Integer placesTotales;
    private Integer placesDisponibles;
    private String categorie;
    private Boolean actif;
    private Double tauxRemplissage;
}
