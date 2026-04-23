package com.theatre.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application principale du microservice de paiement
 */
@SpringBootApplication
public class PaymentServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
        System.out.println("\n" +
            "╔════════════════════════════════════════════╗\n" +
            "║   Payment Service - Démarré !             ║\n" +
            "║   API: http://localhost:8082/payment      ║\n" +
            "║   PayPal: Sandbox Mode                    ║\n" +
            "╚════════════════════════════════════════════╝\n");
    }
}
