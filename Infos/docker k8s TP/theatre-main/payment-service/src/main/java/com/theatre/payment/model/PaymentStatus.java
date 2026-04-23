package com.theatre.payment.model;

/**
 * Enum représentant les statuts de paiement
 */
public enum PaymentStatus {
    CREATED,       // Paiement créé, en attente
    APPROVED,      // Approuvé par PayPal
    COMPLETED,     // Paiement complété
    FAILED,        // Échec du paiement
    CANCELLED,     // Paiement annulé
    REFUNDED       // Paiement remboursé
}
