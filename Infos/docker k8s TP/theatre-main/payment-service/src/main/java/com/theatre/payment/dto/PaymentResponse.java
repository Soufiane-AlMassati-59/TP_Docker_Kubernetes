package com.theatre.payment.dto;

import com.theatre.payment.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Réponse d'un paiement
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long id;
    private Long reservationId;
    private String paypalOrderId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String approvalUrl;
    private String errorMessage;
}
