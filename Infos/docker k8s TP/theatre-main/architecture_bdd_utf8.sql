-- ============================================
-- Base de données : Plateforme de Billetterie
-- Encodage : UTF-8
-- ============================================

-- Configuration de l'encodage client
SET client_encoding = 'UTF8';

-- Suppression des tables si elles existent
DROP TABLE IF EXISTS billets CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS spectacles CASCADE;
DROP TABLE IF EXISTS utilisateurs CASCADE;

-- ============================================
-- Table UTILISATEURS
-- ============================================
CREATE TABLE utilisateurs (
    id_utilisateur SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actif BOOLEAN DEFAULT TRUE
);

-- Index pour optimiser les recherches
CREATE INDEX idx_utilisateur_email ON utilisateurs(email);
CREATE INDEX idx_utilisateur_role ON utilisateurs(role);

-- ============================================
-- Table SPECTACLES
-- ============================================
CREATE TABLE spectacles (
    id_spectacle SERIAL PRIMARY KEY,
    titre VARCHAR(200) NOT NULL,
    description TEXT,
    date_spectacle DATE NOT NULL,
    heure_debut TIME NOT NULL,
    prix_unitaire DECIMAL(10, 2) NOT NULL CHECK (prix_unitaire >= 0),
    image_url VARCHAR(500),
    places_totales INTEGER NOT NULL CHECK (places_totales > 0),
    places_disponibles INTEGER NOT NULL CHECK (places_disponibles >= 0),
    categorie VARCHAR(50) DEFAULT 'Théâtre',
    actif BOOLEAN DEFAULT TRUE,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_places CHECK (places_disponibles <= places_totales)
);

-- Index pour optimiser les recherches
CREATE INDEX idx_spectacle_date ON spectacles(date_spectacle);
CREATE INDEX idx_spectacle_actif ON spectacles(actif);
CREATE INDEX idx_spectacle_categorie ON spectacles(categorie);

-- ============================================
-- Table RESERVATIONS
-- ============================================
CREATE TABLE reservations (
    id_reservation SERIAL PRIMARY KEY,
    id_utilisateur INTEGER NOT NULL,
    id_spectacle INTEGER NOT NULL,
    date_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantite INTEGER NOT NULL CHECK (quantite > 0),
    montant_total DECIMAL(10, 2) NOT NULL CHECK (montant_total >= 0),
    statut VARCHAR(20) DEFAULT 'CONFIRMEE' CHECK (statut IN ('EN_ATTENTE', 'CONFIRMEE', 'ANNULEE', 'EXPIREE')),
    code_confirmation VARCHAR(50) UNIQUE NOT NULL,
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id_utilisateur) ON DELETE CASCADE,
    FOREIGN KEY (id_spectacle) REFERENCES spectacles(id_spectacle) ON DELETE RESTRICT
);

-- Index pour optimiser les recherches
CREATE INDEX idx_reservation_utilisateur ON reservations(id_utilisateur);
CREATE INDEX idx_reservation_spectacle ON reservations(id_spectacle);
CREATE INDEX idx_reservation_statut ON reservations(statut);
CREATE INDEX idx_reservation_date ON reservations(date_reservation);

-- ============================================
-- Table BILLETS
-- ============================================
CREATE TABLE billets (
    id_billet SERIAL PRIMARY KEY,
    id_reservation INTEGER NOT NULL,
    numero_place VARCHAR(10),
    code_qr VARCHAR(255) UNIQUE NOT NULL,
    date_generation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    utilise BOOLEAN DEFAULT FALSE,
    date_utilisation TIMESTAMP,
    FOREIGN KEY (id_reservation) REFERENCES reservations(id_reservation) ON DELETE CASCADE
);

-- Index pour optimiser les recherches
CREATE INDEX idx_billet_reservation ON billets(id_reservation);
CREATE INDEX idx_billet_code_qr ON billets(code_qr);
CREATE INDEX idx_billet_utilise ON billets(utilise);

-- ============================================
-- DONNÉES DE TEST
-- ============================================

-- Insertion d'utilisateurs
INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role) VALUES
('Admin', 'Système', 'admin@theatre.com', '$2a$10$xqUH.ZGvKRPLzjL8n9B3SeO8cLqJQCYGqNzD3P5wKrxJQGxqKQKKi', 'ADMIN'), -- password: admin123
('Dupont', 'Jean', 'jean.dupont@email.com', '$2a$10$xqUH.ZGvKRPLzjL8n9B3SeO8cLqJQCYGqNzD3P5wKrxJQGxqKQKKi', 'USER'), -- password: user123
('Martin', 'Sophie', 'sophie.martin@email.com', '$2a$10$xqUH.ZGvKRPLzjL8n9B3SeO8cLqJQCYGqNzD3P5wKrxJQGxqKQKKi', 'USER'),
('Bernard', 'Pierre', 'pierre.bernard@email.com', '$2a$10$xqUH.ZGvKRPLzjL8n9B3SeO8cLqJQCYGqNzD3P5wKrxJQGxqKQKKi', 'USER');

-- Insertion de spectacles
INSERT INTO spectacles (titre, description, date_spectacle, heure_debut, prix_unitaire, image_url, places_totales, places_disponibles, categorie) VALUES
('Le Malade Imaginaire', 'Comédie-ballet de Molière. Une satire mordante du monde médical du XVIIe siècle.', '2025-11-15', '20:00:00', 35.00, 'https://images.unsplash.com/photo-1503095396549-807759245b35', 200, 200, 'Comédie'),
('Hamlet', 'Tragédie de Shakespeare. Le prince du Danemark face à la vengeance et à la folie.', '2025-11-22', '19:30:00', 42.00, 'https://images.unsplash.com/photo-1507676184212-d03ab07a01bf', 250, 250, 'Tragédie'),
('Cyrano de Bergerac', 'Drame héroïque d''Edmond Rostand. L''histoire d''un poète au grand cœur et au grand nez.', '2025-12-01', '20:30:00', 38.00, 'https://images.unsplash.com/photo-1514306191717-452ec28c7814', 180, 180, 'Drame'),
('Le Tartuffe', 'Comédie de Molière dénonçant l''hypocrisie religieuse et la manipulation.', '2025-12-10', '20:00:00', 35.00, 'https://images.unsplash.com/photo-1533563906091-fdfdffc3e3c4', 200, 200, 'Comédie'),
('Roméo et Juliette', 'Tragédie romantique de Shakespeare. L''histoire d''amour la plus célèbre au monde.', '2025-12-20', '19:00:00', 40.00, 'https://images.unsplash.com/photo-1516450360452-9312f5e86fc7', 220, 220, 'Tragédie'),
('En Attendant Godot', 'Pièce de Samuel Beckett. Une réflexion absurde sur l''attente et l''existence.', '2026-01-05', '20:00:00', 32.00, 'https://images.unsplash.com/photo-1470229722913-7c0e2dbbafd3', 150, 150, 'Théâtre Contemporain');

-- Insertion de réservations de test
INSERT INTO reservations (id_utilisateur, id_spectacle, quantite, montant_total, code_confirmation) VALUES
(2, 1, 2, 70.00, 'CONF-2024-001'),
(3, 2, 3, 126.00, 'CONF-2024-002'),
(4, 1, 1, 35.00, 'CONF-2024-003');

-- Mise à jour des places disponibles
UPDATE spectacles SET places_disponibles = places_disponibles - 2 WHERE id_spectacle = 1;
UPDATE spectacles SET places_disponibles = places_disponibles - 3 WHERE id_spectacle = 2;

-- Insertion de billets
INSERT INTO billets (id_reservation, numero_place, code_qr) VALUES
(1, 'A12', 'QR-CONF-2024-001-1'),
(1, 'A13', 'QR-CONF-2024-001-2'),
(2, 'B05', 'QR-CONF-2024-002-1'),
(2, 'B06', 'QR-CONF-2024-002-2'),
(2, 'B07', 'QR-CONF-2024-002-3'),
(3, 'C10', 'QR-CONF-2024-003-1');

-- ============================================
-- VUES UTILES
-- ============================================

-- Vue pour les statistiques de ventes
CREATE OR REPLACE VIEW vue_statistiques_ventes AS
SELECT 
    s.id_spectacle,
    s.titre,
    s.date_spectacle,
    s.prix_unitaire,
    COUNT(r.id_reservation) as nombre_reservations,
    COALESCE(SUM(r.quantite), 0) as billets_vendus,
    s.places_totales - s.places_disponibles as places_reservees,
    COALESCE(SUM(r.montant_total), 0) as revenu_total,
    ROUND((s.places_totales - s.places_disponibles) * 100.0 / s.places_totales, 2) as taux_remplissage
FROM spectacles s
LEFT JOIN reservations r ON s.id_spectacle = r.id_spectacle AND r.statut = 'CONFIRMEE'
GROUP BY s.id_spectacle, s.titre, s.date_spectacle, s.prix_unitaire, s.places_totales, s.places_disponibles;

-- Vue pour l'historique utilisateur
CREATE OR REPLACE VIEW vue_historique_utilisateur AS
SELECT 
    u.id_utilisateur,
    u.nom,
    u.prenom,
    u.email,
    r.id_reservation,
    r.date_reservation,
    r.code_confirmation,
    r.statut,
    s.titre as spectacle_titre,
    s.date_spectacle,
    s.heure_debut,
    r.quantite,
    r.montant_total
FROM utilisateurs u
JOIN reservations r ON u.id_utilisateur = r.id_utilisateur
JOIN spectacles s ON r.id_spectacle = s.id_spectacle
ORDER BY r.date_reservation DESC;

-- ============================================
-- FONCTIONS UTILES
-- ============================================

-- Fonction pour générer un code de confirmation unique
CREATE OR REPLACE FUNCTION generer_code_confirmation()
RETURNS VARCHAR(50) AS $$
DECLARE
    nouveau_code VARCHAR(50);
    code_existe BOOLEAN;
BEGIN
    LOOP
        nouveau_code := 'CONF-' || TO_CHAR(CURRENT_DATE, 'YYYY') || '-' || LPAD(FLOOR(RANDOM() * 999999)::TEXT, 6, '0');
        SELECT EXISTS(SELECT 1 FROM reservations WHERE code_confirmation = nouveau_code) INTO code_existe;
        EXIT WHEN NOT code_existe;
    END LOOP;
    RETURN nouveau_code;
END;
$$ LANGUAGE plpgsql;

-- Fonction pour vérifier la disponibilité
CREATE OR REPLACE FUNCTION verifier_disponibilite(p_id_spectacle INTEGER, p_quantite INTEGER)
RETURNS BOOLEAN AS $$
DECLARE
    places_dispo INTEGER;
BEGIN
    SELECT places_disponibles INTO places_dispo
    FROM spectacles
    WHERE id_spectacle = p_id_spectacle AND actif = TRUE;
    
    RETURN places_dispo >= p_quantite;
END;
$$ LANGUAGE plpgsql;

-- ============================================
-- TRIGGERS
-- ============================================

-- Trigger pour valider les réservations
CREATE OR REPLACE FUNCTION valider_reservation()
RETURNS TRIGGER AS $$
BEGIN
    -- Vérifier la disponibilité
    IF NOT verifier_disponibilite(NEW.id_spectacle, NEW.quantite) THEN
        RAISE EXCEPTION 'Places insuffisantes pour ce spectacle';
    END IF;
    
    -- Générer un code de confirmation si non fourni
    IF NEW.code_confirmation IS NULL THEN
        NEW.code_confirmation := generer_code_confirmation();
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_valider_reservation
BEFORE INSERT ON reservations
FOR EACH ROW
EXECUTE FUNCTION valider_reservation();

COMMENT ON DATABASE postgres IS 'Base de données de la plateforme de billetterie théâtrale';