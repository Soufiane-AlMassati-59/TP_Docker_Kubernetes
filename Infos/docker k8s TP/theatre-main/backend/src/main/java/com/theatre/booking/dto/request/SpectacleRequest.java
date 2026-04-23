package com.theatre.booking.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO pour la création/modification d'un spectacle
 * Pattern: Data Transfer Object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpectacleRequest {
    
    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 200, message = "Le titre ne peut pas dépasser 200 caractères")
    private String titre;
    
    private String description;
    
    @NotNull(message = "La date du spectacle est obligatoire")
    @Future(message = "La date du spectacle doit être dans le futur")
    private LocalDate dateSpectacle;
    
    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime heureDebut;
    
    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
    private BigDecimal prixUnitaire;
    
    private String imageUrl;
    
    @NotNull(message = "Le nombre de places est obligatoire")
    @Min(value = 1, message = "Il doit y avoir au moins 1 place")
    private Integer placesTotales;
    
    @Size(max = 50, message = "La catégorie ne peut pas dépasser 50 caractères")
    private String categorie;
}
