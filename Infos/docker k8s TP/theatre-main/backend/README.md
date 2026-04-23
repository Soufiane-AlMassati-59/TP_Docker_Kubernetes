# 🎭 Theatre Booking Platform - Backend API

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red.svg)](https://maven.apache.org/)
[![Build](https://img.shields.io/badge/Build-Passing-brightgreen.svg)]()

> API REST complète pour la gestion de réservations de spectacles théâtraux

---

## 📋 Table des Matières

- [Fonctionnalités](#-fonctionnalités)
- [Architecture](#-architecture)
- [Installation](#-installation)
- [Configuration](#️-configuration)
- [Démarrage](#-démarrage)
- [API Endpoints](#-api-endpoints)
- [Technologies](#-technologies)
- [Documentation](#-documentation)

---

## ✨ Fonctionnalités

### Gestion des Utilisateurs
- ✅ Inscription avec validation email
- ✅ Authentification JWT (simplifié)
- ✅ Rôles USER et ADMIN
- ✅ Profil utilisateur

### Gestion des Spectacles
- ✅ CRUD complet (Admin)
- ✅ Liste des spectacles disponibles
- ✅ Recherche par titre
- ✅ Filtrage par date
- ✅ Gestion des places disponibles

### Système de Réservation
- ✅ Création de réservations
- ✅ Calcul automatique du montant
- ✅ Vérification de disponibilité
- ✅ Génération de code de confirmation
- ✅ Annulation de réservations
- ✅ Historique des réservations

### Billets
- ✅ Génération automatique de billets
- ✅ Codes QR uniques
- ✅ Numéros de place
- ✅ Validation d'utilisation

### Statistiques (Admin)
- ✅ Nombre total de réservations
- ✅ Billets vendus
- ✅ Revenu total
- ✅ Taux de remplissage moyen
- ✅ Spectacles populaires

---

## 🏗️ Architecture

### Pattern 3-Tier
```
┌─────────────────┐
│  Presentation   │  Controllers REST (API)
│     Layer       │  - AuthController
│                 │  - SpectacleController
│                 │  - ReservationController
│                 │  - StatisticsController
└────────┬────────┘
         │
┌────────▼────────┐
│   Business      │  Services
│     Logic       │  - UtilisateurService
│     Layer       │  - SpectacleService
│                 │  - ReservationService
│                 │  - BilletService
│                 │  - StatisticsService
└────────┬────────┘
         │
┌────────▼────────┐
│   Data Access   │  Repositories
│     Layer       │  - Spring Data JPA
│                 │  - PostgreSQL
└─────────────────┘
```

### Design Patterns
1. **MVC** - Séparation Controller/Service/Repository
2. **Repository Pattern** - Abstraction d'accès aux données
3. **Service Layer** - Logique métier centralisée
4. **DTO Pattern** - Transfert de données API
5. **Factory Pattern** - Génération de billets
6. **Singleton** - Beans Spring
7. **Builder Pattern** - Construction d'objets (Lombok)

---

## 🚀 Installation

### Prérequis
- **Java 17** ou supérieur
- **Maven 3.8** ou supérieur
- **PostgreSQL 15** ou supérieur
- **IDE** : IntelliJ IDEA, Eclipse, ou VS Code

### 1. Cloner le Projet
```bash
git clone <votre-repo>
cd backend
```

### 2. Base de Données
```bash
# Connexion PostgreSQL
psql -U postgres

# Créer la base
CREATE DATABASE theatre_db;

# Appliquer le schéma
\c theatre_db
\i ../architecture_bdd.sql
```

### 3. Configuration
Éditer `src/main/resources/application.yml` :
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/theatre_db
    username: votre_user
    password: votre_password
```

### 4. Compiler
```bash
mvn clean package -DskipTests
```

---

## ⚙️ Configuration

### application.yml
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/theatre_db
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false

jwt:
  secret: votre-secret-key-super-securise-minimum-256-bits
  expiration: 86400000 # 24 heures
```

### Variables d'Environnement (optionnel)
```bash
export DB_URL=jdbc:postgresql://localhost:5432/theatre_db
export DB_USER=postgres
export DB_PASSWORD=password
export JWT_SECRET=your-secret-key
```

---

## 🎬 Démarrage

### Option 1 : Maven
```bash
mvn spring-boot:run
```

### Option 2 : JAR
```bash
java -jar target/booking-platform-1.0.0.jar
```

### Option 3 : Script
```bash
# Windows
..\start-backend.bat

# Linux/Mac
..\start-backend.sh
```

### Vérification
```bash
# Health check
curl http://localhost:8080/actuator/health

# Swagger UI
http://localhost:8080/swagger-ui.html
```

---

## 🔌 API Endpoints

### Authentification
| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/api/auth/register` | Inscription | Non |
| POST | `/api/auth/login` | Connexion | Non |
| GET | `/api/auth/verify` | Vérifier token | Oui |

### Spectacles
| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/spectacles` | Liste tous | Non |
| GET | `/api/spectacles/{id}` | Détail | Non |
| GET | `/api/spectacles/upcoming` | À venir | Non |
| GET | `/api/spectacles/search?query=` | Recherche | Non |
| POST | `/api/spectacles` | Créer | Admin |
| PUT | `/api/spectacles/{id}` | Modifier | Admin |
| DELETE | `/api/spectacles/{id}` | Supprimer | Admin |

### Réservations
| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/api/reservations` | Créer | Oui |
| GET | `/api/reservations/mes-reservations` | Mes réservations | Oui |
| GET | `/api/reservations/{id}` | Détail | Oui |
| POST | `/api/reservations/{id}/annuler` | Annuler | Oui |
| GET | `/api/reservations/admin/all` | Toutes | Admin |

### Statistiques
| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/statistics/global` | Statistiques | Admin |
| GET | `/api/statistics/popular?limit=5` | Populaires | Non |

---

## 🛠️ Technologies

### Core
- **Java 17** - Langage
- **Spring Boot 3.2.0** - Framework
- **Spring Data JPA** - ORM
- **Spring Security** - Sécurité
- **PostgreSQL 15** - Base de données

### Librairies
- **Lombok** - Réduction boilerplate
- **JWT (jjwt)** - Authentification
- **Springdoc OpenAPI** - Documentation Swagger
- **Hibernate Validator** - Validation
- **Jackson** - JSON

### Build & Tools
- **Maven** - Gestion dépendances
- **JUnit 5** - Tests
- **Mockito** - Mocks

---

## 📚 Documentation

### Complète
- **[QUICK_START.md](../QUICK_START.md)** - Guide démarrage rapide
- **[ETAT_DU_PROJET.md](../ETAT_DU_PROJET.md)** - État complet du projet
- **[RECAPITULATIF_CORRECTIONS.md](../RECAPITULATIF_CORRECTIONS.md)** - Liste des corrections

### Détaillée
- **[DOCUMENTATION_TECHNIQUE.md](../docs/DOCUMENTATION_TECHNIQUE.md)** - Architecture et patterns (8 pages)
- **[RAPPORT_SYNTHESE.md](../docs/RAPPORT_SYNTHESE.md)** - Rapport complet (10 pages)
- **[GUIDE_DEPLOIEMENT.md](../docs/GUIDE_DEPLOIEMENT.md)** - Déploiement production (6 pages)

### API
- **Swagger UI** : http://localhost:8080/swagger-ui.html
- **OpenAPI JSON** : http://localhost:8080/v3/api-docs

---

## 🧪 Tests

### Lancer les Tests
```bash
# Tous les tests
mvn test

# Un test spécifique
mvn test -Dtest=UtilisateurServiceTest

# Avec couverture
mvn clean test jacoco:report
```

### Tests Manuels avec curl

#### Inscription
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@email.com",
    "motDePasse": "Password123!"
  }'
```

#### Connexion
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jean.dupont@email.com",
    "motDePasse": "Password123!"
  }'
```

#### Liste Spectacles
```bash
curl http://localhost:8080/api/spectacles
```

#### Créer Réservation
```bash
curl -X POST http://localhost:8080/api/reservations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "spectacleId": 1,
    "quantite": 2
  }'
```

---

## 📦 Structure du Projet

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/theatre/booking/
│   │   │   ├── TheatreBookingApplication.java
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── SpectacleController.java
│   │   │   │   ├── ReservationController.java
│   │   │   │   └── StatisticsController.java
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   ├── SpectacleRequest.java
│   │   │   │   │   └── ReservationRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── SpectacleResponse.java
│   │   │   │       ├── ReservationResponse.java
│   │   │   │       ├── BilletResponse.java
│   │   │   │       └── StatisticsResponse.java
│   │   │   ├── model/
│   │   │   │   ├── Role.java
│   │   │   │   ├── StatutReservation.java
│   │   │   │   ├── Utilisateur.java
│   │   │   │   ├── Spectacle.java
│   │   │   │   ├── Reservation.java
│   │   │   │   └── Billet.java
│   │   │   ├── repository/
│   │   │   │   ├── UtilisateurRepository.java
│   │   │   │   ├── SpectacleRepository.java
│   │   │   │   ├── ReservationRepository.java
│   │   │   │   └── BilletRepository.java
│   │   │   └── service/
│   │   │       ├── UtilisateurService.java
│   │   │       ├── SpectacleService.java
│   │   │       ├── ReservationService.java
│   │   │       ├── BilletService.java
│   │   │       └── StatisticsService.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-prod.yml
│   └── test/
│       └── java/com/theatre/booking/
├── target/
│   └── booking-platform-1.0.0.jar
├── pom.xml
└── README.md
```

---

## 🐛 Dépannage

### Port déjà utilisé
```yaml
# application.yml
server:
  port: 8081
```

### Connexion base de données échoue
```bash
# Vérifier PostgreSQL
psql -U postgres -h localhost -p 5432

# Tester connexion
psql -U postgres -d theatre_db
```

### Erreurs de compilation
```bash
# Nettoyer et recompiler
mvn clean compile

# Forcer téléchargement dépendances
mvn clean install -U
```

---

## 🤝 Contribution

### Développement
```bash
# Créer une branche
git checkout -b feature/ma-fonctionnalite

# Coder et tester
mvn test

# Commit
git add .
git commit -m "feat: ajout de ma fonctionnalité"

# Push
git push origin feature/ma-fonctionnalite
```

### Standards
- **Code Style** : Google Java Style Guide
- **Commits** : Conventional Commits
- **Tests** : Couverture > 80%

---

## 📄 Licence

Ce projet est développé dans le cadre académique M1 DevOps.

---

## 👥 Auteur

**Projet M1 DevOps** - Qualité de Données
Plateforme de Réservation de Spectacles

---

## 🎯 Roadmap

### Version 1.0 ✅
- [x] Backend API REST complet
- [x] Authentification utilisateurs
- [x] Gestion spectacles
- [x] Système de réservation
- [x] Génération billets
- [x] Statistiques admin

### Version 1.1 (À venir)
- [ ] JWT avec bibliothèque complète
- [ ] Tests unitaires complets
- [ ] Gestion d'erreurs avancée
- [ ] Pagination des listes
- [ ] Filtres avancés

### Version 2.0 (Future)
- [ ] Frontend React
- [ ] Application mobile
- [ ] Notifications email
- [ ] Paiement en ligne
- [ ] Génération PDF billets

---

**Documentation complète** : Voir dossier `../docs/`

**Support** : Consulter `QUICK_START.md` et `RECAPITULATIF_CORRECTIONS.md`
