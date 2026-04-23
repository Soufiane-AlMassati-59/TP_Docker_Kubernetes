package com.theatre.booking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la création d'une réservation
 * Pattern: Data Transfer Object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    
    @NotNull(message = "L'identifiant du spectacle est obligatoire")
    private Long spectacleId;
    
    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantite;
}
