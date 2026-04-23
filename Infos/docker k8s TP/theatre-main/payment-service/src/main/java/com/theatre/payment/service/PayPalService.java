package com.theatre.payment.service;

import com.theatre.payment.dto.PaymentRequest;
import com.theatre.payment.dto.PaymentResponse;
import com.theatre.payment.model.Payment;
import com.theatre.payment.model.PaymentStatus;
import com.theatre.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayPalService {
    
    private final PaymentRepository paymentRepository;
    
    /**
     * VERSION SIMPLIFIÉE : Créer un paiement (simulation)
     */
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        try {
            log.info("Création d'un paiement pour la réservation {}", request.getReservationId());
            
            String mockPayPalOrderId = "MOCK-ORDER-" + UUID.randomUUID().toString().substring(0, 8);
            String mockApprovalUrl = "https://www.sandbox.paypal.com/checkoutnow?token=" + mockPayPalOrderId;
            
            Payment payment = Payment.builder()
                    .reservationId(request.getReservationId())
                    .paypalOrderId(mockPayPalOrderId)
                    .amount(request.getAmount())
                    .currency(request.getCurrency() != null ? request.getCurrency() : "EUR")
                    .status(PaymentStatus.CREATED)
                    .approvalUrl(mockApprovalUrl)
                    .build();
            
            payment = paymentRepository.save(payment);
            
            log.info("Paiement créé avec succès : {}", mockPayPalOrderId);
            
            return PaymentResponse.builder()
                    .id(payment.getId())
                    .reservationId(payment.getReservationId())
                    .paypalOrderId(mockPayPalOrderId)
                    .amount(payment.getAmount())
                    .currency(payment.getCurrency())
                    .status(PaymentStatus.CREATED)
                    .approvalUrl(mockApprovalUrl)
                    .build();
                    
        } catch (Exception e) {
            log.error("Erreur lors de la création du paiement", e);
            throw new RuntimeException("Erreur PayPal : " + e.getMessage());
        }
    }
    
    @Transactional
    public PaymentResponse capturePayment(String paypalOrderId) {
        log.info("Capture du paiement : {}", paypalOrderId);
        
        Payment payment = paymentRepository.findByPaypalOrderId(paypalOrderId)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaypalCaptureId("CAPTURE-" + UUID.randomUUID().toString().substring(0, 8));
        payment = paymentRepository.save(payment);
        
        return PaymentResponse.builder()
                .id(payment.getId())
                .reservationId(payment.getReservationId())
                .paypalOrderId(payment.getPaypalOrderId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .build();
    }
}
