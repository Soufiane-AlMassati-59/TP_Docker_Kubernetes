# Livrables Projet Billetterie Théâtre

Statut : COMPLET ET OPÉRATIONNEL

## Code Source

### Backend (Spring Boot)

```
backend/src/main/java/com/theatre/booking/
├── model/         # Entities (Utilisateur, Spectacle, Reservation, Billet, Role, Statuts)
├── dto/           # Request/Response (Login, Register, Spectacle, Reservation, Billet, Statistics)
├── repository/    # Repositories Spring Data
├── security/      # JwtUtil
├── service/       # Business Logic (Utilisateur, Spectacle, Reservation, Billet, Statistics)
├── controller/    # REST (Auth, Spectacle, Reservation, Statistics)
├── config/        # CorsConfig
└── TheatreBookingApplication.java
```

### Frontend (React)

```
frontend/src/
├── components/    # SpectaclesList, SpectacleDetail, Statistics, Login, Register, Header, Cart, Confirmation
├── context/       # CartContext (état global panier)
├── services/      # api.js (appels API centralisés)
├── styles/        # Auth, Cart, Confirmation, Header, SpectaclesList CSS
├── App.jsx        # Routes React Router
└── main.jsx       # Point d'entrée
```

## Base de Données

### Schéma SQL
- `architecture_bdd.sql` - Script complet
- 4 tables : utilisateurs, spectacles, reservations, billets
- 12 index optimisation
- 2 vues : statistiques, historique
- 2 fonctions PL/pgSQL
- 1 trigger validation
- Contraintes : FK, CHECK, UNIQUE
- Données test : 6 spectacles, 3 réservations, 6 billets

## 3. DOCUMENTATION TECHNIQUE

### 3.1 Modélisation MERISE

#### MCD (Modèle Conceptuel de Données)
- `docs/uml/merise.mmd` - Diagramme Mermaid
- Entités : UTILISATEUR, SPECTACLE, RESERVATION, BILLET
- Relations : 1,n / n,1 / 1,n
- Cardinalités définies

#### MLD (Modèle Logique de Données)
- Inclus dans `architecture_bdd.sql`
- Inclus dans `docs/DOCUMENTATION_TECHNIQUE.md`
- Normalisation 3NF respectée

---

### 3.2 Diagrammes UML

#### Diagramme de Cas d'Utilisation
- `docs/uml/cas_d_utilisation.mmd` - Format Mermaid
- 11 cas d'utilisation
- 2 acteurs : Utilisateur, Administrateur
- Relations extend/include

#### Diagramme de Classes Complet
- `docs/uml/diagramme_classes_complet.mmd` - Format Mermaid
- 4 entités JPA
- 2 enumerations
- 8 DTOs
- 4 Repositories
- 5 Services
- 4 Controllers
- Relations et multiplicités

#### Diagramme de Séquence
- Processus complet de réservation
- 5 scénarios détaillés
- Interactions entre toutes les couches

#### Diagramme d'États
- États utilisateur : authentifié/non authentifié
- États réservation : en attente, confirmée, annulée
- Transitions complètes

---

### 3.3 Architecture 3-Tiers

#### Schémas d'Architecture
- Schéma 3-Tiers ASCII art dans `docs/DOCUMENTATION_TECHNIQUE.md`
- Schéma avec composants détaillés
- Flux de données entre couches
- Technologies par couche

---

### 3.4 Documentation des Design Patterns

#### Patterns Documentés
1. **MVC** - Architecture globale
2. **Repository Pattern** - Abstraction données
3. **Service Layer Pattern** - Logique métier
4. **DTO Pattern** - Transfer objets
5. **Factory Pattern** - Création billets
6. **Singleton Pattern** - Configuration JWT
7. **Builder Pattern** - Construction objets

Chaque pattern inclut :
- Explication du pattern
- Code d'exemple
- Justification du choix
- Avantages

---
## Documentation

### README.md
Description, architecture, stack, installation, endpoints API, fonctionnalités, design patterns, commandes.

### docs/DOCUMENTATION_TECHNIQUE.md
Architecture 3-Tiers, MERISE, UML, Design Patterns exemples, principes REST, sécurité.

### docs/DEMARRAGE.md
Prérequis, installation locale, config BDD, lancement backend/frontend, vérification, troubleshooting.

## Tests

### Intégration Effectuée
- Backend compilé/démarré (port 8080)
- BDD connectée/peuplée
- Endpoints testés (200 OK)
- Frontend démarré (port 3000)
- CORS + Proxy Vite configurés
- Interface affiche données correctement

### Données Test
- 6 spectacles, 3 réservations, 6 billets
- Statistiques : 231€ revenus, 36.7% taux remplissage

## Configuration & Scripts

### Fichiers Config
- `pom.xml` - Maven Spring Boot 3.2.0, Java 17
- `frontend/package.json` - npm React 18, Vite 5
- `application.yml` - PostgreSQL config
- `frontend/vite.config.js` - Proxy API

### Scripts
**Windows** : START_ALL.bat, START_BACKEND.bat, START_FRONTEND.bat, install-dependencies.bat

**Linux/Mac** : start-backend.sh

**Test** : TEST_API.html - Interface interactive test endpoints

### Organisation Docs
- docs/ avec 6 fichiers markdown
- docs/uml/ avec 3 diagrammes Mermaid
- README.md racine + docs/README.md index

## Livrables Finaux

### Documents
README, doc technique, guide démarrage/utilisation, état projet, 4 UML, MERISE (MCD+MLD), schéma 3-Tiers.

### Code
Backend Spring Boot complet, Frontend React complet, script SQL BDD, CORS/proxy, 4 Controllers, 5 Services, 4 Repositories, 8 DTOs.

### Démonstration
App web fonctionnelle responsive, API REST 4 endpoints principaux, BDD peuplée, frontend/backend testés, 3 pages interface, scripts auto Windows/Linux, TEST_API.html interactif.

## Points Forts

Modélisation exemplaire MERISE/UML, documentation exhaustive 6 fichiers, architecture 3-Tiers solide, 7 design patterns, BDD robuste (index/vues/triggers), services métier complets, API REST 15+ endpoints, frontend React moderne, intégration CORS/proxy réussie, tests validés données réelles, scripts automatisés, outil test API.

## Technologies

**Backend** : Spring Boot 3.2.0, Java 17/22, PostgreSQL 18, Spring Data JPA, Maven, context-path /api

**Frontend** : React 18, Vite 5, React Router v6, Axios, CSS moderne

**BDD** : PostgreSQL 18, base theatre_db, user theatre_user, port 5432

**Serveurs** : Backend :8080, Frontend :3000, API http://localhost:8080/api
