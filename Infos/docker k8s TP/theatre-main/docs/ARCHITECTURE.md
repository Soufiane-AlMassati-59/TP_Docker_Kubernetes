# Architecture Plateforme Billetterie Théâtre

## Technologies

### Backend
- Spring Boot 3.2.0
- Java 17
- Maven
- PostgreSQL 18
- Hibernate/JPA
- Spring Security
- SpringDoc OpenAPI (Swagger)

### Frontend
- React 18
- Vite 5
- React Router DOM v6
- CSS3
- Fetch API

## Structure Backend

### Packages

```
com.theatre.booking/
 config/       # Security, CORS, OpenAPI
 controller/   # REST Controllers
 dto/          # Request/Response
 model/        # JPA Entities
 repository/   # Spring Data
 security/     # JWT
 service/      # Business Logic
```

### Sécurité JWT

JwtUtil : Gestion tokens JWT
- generateToken(userId, email, role): String
- validateToken(token): boolean
- getEmailFromToken(token): String
- getUserIdFromToken(token): Long
- getRoleFromToken(token): String
- Algorithme: HS256, Expiration: 24h

### Entités

**Spectacle**
- id, titre, description, genre
- dateSpectacle, heureDebut, dureeMinutes
- prixUnitaire, placesTotales, placesDisponibles
- imageUrl, statut, tauxRemplissage

**Reservation**
- id, utilisateur (FK), spectacle (FK)
- nombrePlaces, montantTotal
- dateReservation, statut, billets

**Billet**
- id, reservation (FK), numeroBillet
- dateEmission, qrCode, statut

**Utilisateur**
- id, nom, prenom, email
- motDePasse (hashé), dateInscription
- actif, roles

**Role**
- id, nom (ROLE_USER, ROLE_ADMIN)

### Relations

```
Utilisateur (1)---(N) Reservation
Spectacle (1)---(N) Reservation
Reservation (1)---(N) Billet
Utilisateur (N)---(N) Role
```

## API REST

Base URL : `http://localhost:8080/api`

### Spectacles `/spectacles`

| Méthode | Endpoint | Auth |
|---------|----------|------|
| GET | `/spectacles` | Non |
| GET | `/spectacles/{id}` | Non |
| GET | `/spectacles/upcoming` | Non |
| GET | `/spectacles/search?query=...` | Non |
| POST | `/spectacles` | Admin |
| PUT | `/spectacles/{id}` | Admin |
| DELETE | `/spectacles/{id}` | Admin |

### Statistiques `/statistics`

| Méthode | Endpoint |
|---------|----------|
| GET | `/statistics/global` |
| GET | `/statistics/popular?limit=10` |

Réponse `/statistics/global` :
```json
{
 "totalSpectacles": 10,
 "totalReservations": 45,
 "totalBillets": 120,
 "chiffreAffairesTotal": 5400.00,
 "tauxRemplissageMoyen": 65.5,
 "spectaclesPopulaires": [...]
}
```

### Réservations `/reservations`

| Méthode | Endpoint | Auth |
|---------|----------|------|
| POST | `/reservations` | User |
| GET | `/reservations/mes-reservations` | User |
| GET | `/reservations/{id}` | User |
| PUT | `/reservations/{id}/cancel` | User |

### Authentification `/auth`

| Méthode | Endpoint |
|---------|----------|
| POST | `/auth/register` |
| POST | `/auth/login` |
| GET | `/auth/verify` |
| GET | `/auth/profile` |

Réponse `/auth/login` :
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": 1,
  "email": "user@example.com",
  "role": "USER"
}
```

## Architecture Frontend

### Structure des dossiers

```
frontend/
 public/ # Fichiers statiques
 src/
   components/ # Composants React
     SpectaclesList.jsx # Liste des spectacles
     SpectacleDetail.jsx # Détails d'un spectacle
     Login.jsx # Page de connexion
     Register.jsx # Page d'inscription
     Cart.jsx # Panier d'achat
     Confirmation.jsx # Confirmation de commande
     Header.jsx # En-tête avec navigation
   context/ # Contextes React
     CartContext.jsx # Gestion globale du panier
   services/ # Services API
     api.js # Service d'appels API avec JWT
   App.jsx # Composant principal
   main.jsx # Point d'entrée
```

### Gestion du panier (CartContext)

Le panier est géré avec React Context API et persiste dans localStorage.

**Fonctions disponibles :**
```javascript
- addToCart(spectacle, quantity) # Ajouter au panier
- removeFromCart(spectacleId) # Retirer du panier
- updateQuantity(spectacleId, quantity) # Modifier la quantité
- clearCart() # Vider le panier
- getTotal() # Calculer le total
- getItemCount() # Nombre d'articles
```

**Structure des données du panier :**
```javascript
cart = [
  {
    spectacle: { id, titre, prixUnitaire, dateSpectacle, ... },
    quantity: 2
  },
  ...
]
```
 src/
 components/ # Composants React
 SpectaclesList.jsx
 SpectacleDetail.jsx
 Statistics.jsx
 services/ # Services API
 api.js
 App.jsx # Composant principal
 App.css # Styles globaux
 main.jsx # Point d'entrée

## Frontend

### Structure

```
frontend/src/
 components/
   SpectaclesList.jsx
   SpectacleDetail.jsx
   Statistics.jsx
   Login.jsx
   Register.jsx
   Header.jsx
   Cart.jsx
   Confirmation.jsx
 context/
   CartContext.jsx
 services/
   api.js
 styles/
 App.jsx
 main.jsx
```

### Routes

| Route | Composant | Description |
|-------|-----------|-------------|
| `/` | SpectaclesList | Liste + recherche |
| `/spectacle/:id` | SpectacleDetail | Détails |
| `/statistics` | Statistics | Tableau de bord |

### Service API

`services/api.js` centralise les appels :

```javascript
class ApiService {
 async getAllSpectacles()
 async getSpectacleById(id)
 async searchSpectacles(query)
 async getGlobalStatistics()
 async getPopularSpectacles(limit)
}
```

Proxy Vite : `/api` → `http://localhost:8080/api`

## Sécurité et CORS

CORS autorise :
- `http://localhost:3000`
- `http://127.0.0.1:3000`
- Méthodes : GET, POST, PUT, DELETE, OPTIONS

Spring Security : Mode développement (permitAll). Mode production : JWT + rôles.

## Base de données

### Configuration
- Base : `theatre_db`
- User : `theatre_user`
- Password : `theatre123`
- Port : 5432

### Tables

```sql
CREATE TABLE spectacles (
 id_spectacle BIGSERIAL PRIMARY KEY,
 titre VARCHAR(255) NOT NULL,
 description TEXT,
 date_heure TIMESTAMP NOT NULL,
 duree INTEGER,
 prix_unitaire DECIMAL(10,2),
 places_disponibles INTEGER,
 created_at TIMESTAMP,
 updated_at TIMESTAMP,
 statut VARCHAR(50)
);

CREATE TABLE utilisateurs (
 id_utilisateur BIGSERIAL PRIMARY KEY,
 nom VARCHAR(100),
 prenom VARCHAR(100),
 email VARCHAR(255) UNIQUE NOT NULL,
 mot_de_passe VARCHAR(255),
 date_inscription TIMESTAMP,
 actif BOOLEAN DEFAULT true
);

CREATE TABLE reservations (
 id_reservation BIGSERIAL PRIMARY KEY,
 id_utilisateur BIGINT REFERENCES utilisateurs(id_utilisateur),
 id_spectacle BIGINT REFERENCES spectacles(id_spectacle),
 nombre_places INTEGER NOT NULL,
 montant_total DECIMAL(10,2),
 date_reservation TIMESTAMP,
 statut VARCHAR(50)
);

CREATE TABLE billets (
 id_billet BIGSERIAL PRIMARY KEY,
 id_reservation BIGINT REFERENCES reservations(id_reservation),
 numero_billet VARCHAR(50) UNIQUE,
 date_emission TIMESTAMP,
 qr_code TEXT,
 statut VARCHAR(50)
);
```

## Design Patterns

### Backend
- MVC : Séparation responsabilités
- DTO : Isolation entités/API
- Repository Pattern : Spring Data JPA
- Service Layer : Logique métier
- Dependency Injection : Spring IoC
- Builder Pattern : Lombok

### Frontend
- Component-Based : React composants réutilisables
- Separation of Concerns : Services API séparés
- Single Source of Truth : useState
- Responsive Design : CSS Grid/Flexbox
- Progressive Enhancement : Erreurs gérées

## Évolutions futures

### Fonctionnalités
- Authentification JWT complète
- Paiement (Stripe, PayPal)
- QR codes billets
- Emails confirmation
- Notation et avis
- Calendrier interactif
- Filtres avancés
- Export PDF

### Technique
- Tests unitaires/intégration
- CI/CD
- Docker
- Cloud (Azure, AWS)
- Monitoring (ELK)
- Cache Redis
- WebSockets temps réel
