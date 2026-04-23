package com.theatre.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Requête pour créer un paiement
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private Long reservationId;
    private BigDecimal amount;
    private String currency;
    private String returnUrl;
    private String cancelUrl;
}
