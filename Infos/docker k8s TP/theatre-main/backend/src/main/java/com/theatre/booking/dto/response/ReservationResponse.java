package com.theatre.booking.dto.response;

import com.theatre.booking.model.StatutReservation;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la réponse contenant les détails d'une réservation
 * Pattern: Data Transfer Object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
    
    private Long id;
    private Long utilisateurId;
    private String utilisateurEmail;
    private String utilisateurNom;
    private Long spectacleId;
    private String spectacleTitre;
    private LocalDateTime dateReservation;
    private Integer quantite;
    private BigDecimal montantTotal;
    private StatutReservation statut;
    private String codeConfirmation;
    private List<BilletResponse> billets;
}
