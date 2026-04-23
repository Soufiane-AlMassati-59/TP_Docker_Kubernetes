# Rapport de Synthèse

Projet : Plateforme Billetterie Théâtre
Équipe : M1 DevOps - Qualité de Données
Date : Octobre 2025
Version : 1.0.0
Statut : Complet et Opérationnel

## Contexte

Moderniser le système de billetterie théâtrale avec plateforme en ligne pour réservations, gestion catalogue et statistiques temps réel.

### Objectifs

**Fonctionnels** : Réservations en ligne, gestion catalogue admin, statistiques temps réel, billets QR codes.

**Techniques** : Architecture 3-Tiers, MVC, API REST documentée, design patterns, modélisation MERISE/UML, sécurité données.

**Pédagogiques** : Maîtrise architecture logicielle, bonnes pratiques, patterns conception, méthodologies modélisation.

## Solutions Techniques

### Architecture 3-Tiers

```
PRÉSENTATION (React Web/Mobile)
       ↓ HTTP/REST + JSON
LOGIQUE MÉTIER (Spring Boot API + Services + JWT)
       ↓ JDBC/JPA
DONNÉES (PostgreSQL + Intégrité référentielle)
```

Avantages : Séparation responsabilités, maintenance facilitée, scalabilité horizontale, testabilité améliorée.

### Stack

**Backend**
- Java 17 (LTS, performances, écosystème)
- Spring Boot 3.2.0 (robuste, auto-configuration)
- Spring Data JPA 3.2.0 (ORM, Repository Pattern)
- Spring Security 6.x (JWT)
- PostgreSQL 15 (ACID, robuste)
- JWT 0.11.5 (authentification stateless)
- Lombok (réduction boilerplate)
- Swagger/OpenAPI 2.2.0 (documentation auto)
- Maven 3.8+

**Frontend**
- React 18.x (composants, Virtual DOM)
- Vite 5.x (build rapide, HMR)
- React Router 6.x (navigation SPA)
- Axios 1.x (HTTP intercepteurs)
- Tailwind CSS 3.x (utility-first)
- React Hook Form 7.x (formulaires)

**Base de Données**
- PostgreSQL 18 : ACID, UTF-8 natif, JSON/JSONB, performance, outils admin riches.

### Design Patterns

| Pattern | Localisation | Objectif |
|---------|--------------|----------|
| MVC | Architecture globale | Séparation Model-View-Controller |
| Repository | repository/ | Abstraction accès données |
| Service Layer | service/ | Logique métier |
| DTO | dto/ | Transfer entre couches |
| Factory | BilletService | Création billets |
| Singleton | JwtUtil | Instance unique JWT |
| Builder | Entities + DTOs | Construction fluide |
| Context | CartContext.jsx | État global React |
| **Interceptor** | `api.js` | Injection automatique JWT |


## 3. Modélisation

### 3.1 MERISE

#### MCD (Modèle Conceptuel de Données)
4 entités principales identifiées :
- **UTILISATEUR** : Acteurs du système (USER/ADMIN) avec authentification JWT
- **SPECTACLE** : Événements à réserver avec images et disponibilités
- **RESERVATION** : Lien utilisateur-spectacle avec montant total
- **BILLET** : Preuve d'achat physique/numérique avec QR codes

**Relations** :
- Un utilisateur passe 0 à n réservations (1,n)
- Une réservation concerne 1 spectacle (n,1)
- Une réservation génère 1 à n billets (1,n)

#### MLD (Modèle Logique)
```sql
UTILISATEUR(#id_utilisateur, nom, prenom, email, mot_de_passe, role, date_inscription, actif)
SPECTACLE(#id_spectacle, titre, description, genre, date_spectacle, heure_debut, 
          duree_minutes, prix_unitaire, image_url, places_totales, places_disponibles)
RESERVATION(#id_reservation, date_reservation, quantite, montant_total, statut, 
           code_confirmation, =>id_utilisateur, =>id_spectacle)
BILLET(#id_billet, numero_place, code_qr, date_generation, utilise, =>id_reservation)
```

### 3.2 UML

#### Diagrammes Réalisés
1. **Diagramme de cas d'utilisation**
 - 15+ cas d'utilisation identifiés
 - 2 acteurs (Utilisateur, Administrateur)
 - Relations include/extend
 - Nouveaux cas : Login, Register, Gérer panier, Payer

2. **Diagramme de classes complet**
 - 4 entités JPA
 - 2 enumerations
 - 10+ DTOs Request/Response
 - 4 Repositories
 - 6 Services (ajout JwtUtil)
 - 4 Controllers
 - Relations détaillées

3. **Diagramme de séquence**
 - Processus complet de réservation avec authentification
 - Workflow panier → paiement → confirmation
 - Interactions entre couches
 - Gestion des transactions

4. **Diagramme d'états**
 - Cycle de vie utilisateur (anonyme → inscrit → connecté)
 - États réservation (pending → confirmed → cancelled)
 - États panier (vide → articles → checkout → payé)

---

## 4. Fonctionnalités Implémentées

### 4.1 Fonctionnalités Utilisateur

| Fonctionnalité | Complexité | Détails |
|----------------|------------|---------|
| Inscription/Connexion | Moyenne | JWT + BCrypt |
| Authentification JWT | Élevée | Tokens 24h, auto-refresh |
| Consultation spectacles | Faible | Liste avec images |
| Recherche spectacles | Faible | Temps réel |
| Détails spectacle | Moyenne | Images, disponibilités |
| Panier d'achat | Élevée | Context API + localStorage |
| Gestion quantités | Moyenne | +/- avec limites |
| Paiement simulé | Moyenne | Modal carte/PayPal |
| Confirmation commande | Moyenne | E-billets avec QR codes |
| Réservation de billets | Élevée | Avec vérification places |
| Consultation réservations | Moyenne | Historique complet |
| Annulation réservation | Moyenne | Avec remboursement |

### 4.2 Fonctionnalités Administrateur

| Fonctionnalité | Complexité |
|----------------|------------|
| Gestion spectacles (CRUD) | Moyenne |
| Statistiques globales | Élevée |
| Spectacles populaires | Moyenne |
| Taux de remplissage | Faible |
| Revenus par spectacle | Moyenne |
| Gestion utilisateurs | Faible |

### 4.3 Fonctionnalités Techniques

| Fonctionnalité | Description |
|----------------|-------------|
| Authentification JWT | Token valide 24h |
| Autorisation RBAC | Rôles USER/ADMIN |
| Validation données | Backend + Frontend |
| Gestion erreurs | Messages explicites |
| Documentation API | Swagger/OpenAPI |
| Tests unitaires | ~60% couverture |
| Logs applicatifs | SLF4J + Logback |
| CORS configuré | Origins multiples |

---

## 5. Défis Rencontrés et Solutions

### 5.1 Gestion de la Concurrence

**Problème** : Deux utilisateurs réservent simultanément les dernières places

**Solution implémentée** :
```java
@Transactional(isolation = Isolation.SERIALIZABLE)
public ReservationResponse creerReservation(...) {
 // Vérification + Réservation atomique
 spectacle.reserverPlaces(quantite);
 // ...
}
```

### 5.2 Sécurité des Mots de Passe

**Problème** : Stockage sécurisé des credentials

**Solution** :
- Hashage BCrypt (salt auto, coût 10)
- Pas de stockage mot de passe en clair
- Validation force mot de passe (min 6 caractères)
## Défis et Solutions

### Encodage UTF-8
Problème : Accents français mal affichés.
Solution : `connection-init-sql: "SET client_encoding TO 'UTF8'"` + recréation base en UTF-8.

### CORS
Problème : Requêtes cross-origin bloquées.
Solution : Configuration `@CrossOrigin` + CorsConfig Spring.

### Validation
Problème : Données invalides côté client.
Solution : Validation double (Frontend + Backend) + annotations Jakarta Validation.

### Performance
Problème : N+1 queries.
Solution : `@Query` avec JOIN FETCH + index sur colonnes recherchées.

## Points Forts

**Architecture** : 3-Tiers strict, 7 design patterns, code maintenable, Swagger auto-généré, JWT + RBAC.

**Modélisation** : MERISE complet (MCD + MLD), UML exhaustif (4 types), base normalisée 3NF, intégrité référentielle.

**Fonctionnel** : Parcours utilisateur fluide, CRUD admin complet, QR codes uniques, annulation flexible.

## Améliorations Futures

**Court Terme** : Tests 80%+, pagination, export PDF billets, emails confirmation.

**Moyen Terme** : Paiement Stripe/PayPal, notifications push, graphiques statistiques, codes promo.

**Long Terme** : ML recommandations, multi-théâtres, places numérotées visuelles, app native iOS/Android.

## Métriques

### Code
- Backend : ~4200 lignes, 40+ classes Java, 20+ endpoints API
- Frontend : ~3500 lignes, 10+ composants React, 1 Context
- Tests : 65% couverture

### Base de Données
- 4 tables, 3 FK, UTF-8, 12 index, 2 vues, 1 trigger

### Documentation
- README.md, Doc technique, 4 UML, 1 MERISE, Guide déploiement

## Retour d'Expérience

**Compétences Acquises** : Architecture 3-Tiers/MVC, Spring Boot/JPA/Security, React moderne hooks/routing, modélisation BDD/optimisation, API REST/JWT/Swagger, 7+ design patterns.

**Méthodologie** : Modélisation first (MERISE/UML avant code), développement itératif par fonctionnalités, documentation continue, revue de code.

**Difficultés** : Gestion transactions (isolation SERIALIZABLE), JWT expiration (refresh token non implémenté), tests async MockMVC.

## Conclusion

### Objectifs Atteints
100% des exigences : Architecture 3-Tiers, MVC, API REST documentée, 7 design patterns, MERISE + UML, application web fonctionnelle, prototype mobile, documentation exhaustive.

### Valeur Ajoutée
Conception architecture robuste, modélisation systèmes complexes, implémentation patterns, documentation professionnelle, sécurisation application web, livraison produit complet.

### Production
Prêt après : Tests charge/sécurité, monitoring Prometheus/Grafana, CI/CD Jenkins/GitLab, déploiement Docker/Kubernetes.
