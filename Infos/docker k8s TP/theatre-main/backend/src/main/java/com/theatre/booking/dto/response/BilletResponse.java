package com.theatre.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour la réponse contenant les détails d'un billet
 * Pattern: Data Transfer Object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BilletResponse {
    
    private Long id;
    private String numeroPlace;
    private String codeQR;
    private LocalDateTime dateGeneration;
    private Boolean utilise;
    private LocalDateTime dateUtilisation;
}
