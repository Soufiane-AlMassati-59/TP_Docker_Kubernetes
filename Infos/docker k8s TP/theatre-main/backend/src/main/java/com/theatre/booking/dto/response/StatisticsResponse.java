package com.theatre.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO pour les statistiques de vente
 * Pattern: Data Transfer Object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsResponse {
    
    private Long totalReservations;
    private Long totalBilletsVendus;
    private BigDecimal revenuTotal;
    private Double tauxRemplissageMoyen;
    private List<SpectacleStats> spectaclesPopulaires;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SpectacleStats {
        private Long spectacleId;
        private String titre;
        private Long nombreReservations;
        private Long billetsVendus;
        private BigDecimal revenu;
        private Double tauxRemplissage;
    }
}
