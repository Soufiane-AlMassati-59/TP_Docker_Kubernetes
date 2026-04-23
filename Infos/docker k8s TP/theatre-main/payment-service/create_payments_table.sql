-- Script SQL pour créer la table payments dans theatre_db
-- À exécuter avant de démarrer payment-service

CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    reservation_id BIGINT,
    paypal_order_id VARCHAR(255) UNIQUE,
    paypal_capture_id VARCHAR(255),
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'EUR',
    status VARCHAR(50) NOT NULL,
    payer_email VARCHAR(255),
    approval_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index pour améliorer les performances
CREATE INDEX IF NOT EXISTS idx_paypal_order_id ON payments(paypal_order_id);
CREATE INDEX IF NOT EXISTS idx_reservation_id ON payments(reservation_id);
CREATE INDEX IF NOT EXISTS idx_status ON payments(status);

-- Afficher les tables
\dt

SELECT 'Table payments créée avec succès!' AS message;
