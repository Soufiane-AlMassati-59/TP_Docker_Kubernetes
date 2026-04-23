package com.theatre.payment.controller;

import com.theatre.payment.dto.PaymentRequest;
import com.theatre.payment.dto.PaymentResponse;
import com.theatre.payment.service.PayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PaymentController {
    
    private final PayPalService payPalService;
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Payment Service OK - PayPal Sandbox Mode");
    }
    
    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = payPalService.createPayment(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/capture/{paypalOrderId}")
    public ResponseEntity<PaymentResponse> capturePayment(@PathVariable String paypalOrderId) {
        PaymentResponse response = payPalService.capturePayment(paypalOrderId);
        return ResponseEntity.ok(response);
    }
}
