# Guide de démarrage

## Structure du projet

```
theatre-booking/
├── backend/              # API Spring Boot
├── frontend/             # Interface React
├── docs/                 # Documentation
├── architecture_bdd.sql  # Script BDD
├── START_ALL.bat         # Lancement auto
└── TEST_API.html         # Test API
```

## Prérequis

- Java 17+
- Maven 3.6+
- Node.js 18+
- PostgreSQL 18

## Configuration base de données

- Base : `theatre_db`
- Utilisateur : `theatre_user`
- Mot de passe : `theatre123`

## Démarrage automatique

### Windows

**START_ALL.bat** - Lance backend et frontend

### Linux/Mac

```bash
chmod +x start-backend.sh
./start-backend.sh
```

## Démarrage manuel

### Backend

```powershell
cd backend
mvn spring-boot:run
```

Accessible sur : http://localhost:8080

### Endpoints principaux

**Spectacles**
- GET `/api/spectacles` - Liste complète
- GET `/api/spectacles/{id}` - Détails
- GET `/api/spectacles/upcoming` - À venir
- GET `/api/spectacles/search?query=...` - Recherche

**Authentification**
- POST `/api/auth/register` - Inscription
- POST `/api/auth/login` - Connexion (retourne JWT token)

**Statistiques**
- GET `/api/statistics/global` - Statistiques globales
- GET `/api/statistics/popular?limit=10` - Populaires

**Documentation**
- GET `/swagger-ui/index.html` - Swagger

Note : Les endpoints protégés nécessitent `Authorization: Bearer <token>`

### Frontend

```powershell
npm install
npm run dev
```

Frontend accessible sur : http://localhost:3000

## Test de l'application

### Interface Web (http://localhost:3000)

Fonctionnalités : Liste des spectacles avec recherche/filtres, authentification (inscription/connexion), panier d'achat avec gestion des quantités, paiement simulé (carte/PayPal), confirmation avec e-billets, statistiques temps réel, détails spectacles avec images.

### Test API

Ouvrir `TEST_API.html` dans un navigateur pour tester tous les endpoints de manière interactive avec résultats JSON.

## Routes

### Backend
- Context path : `/api`
- `/spectacles` → SpectacleController
- `/statistics` → StatisticsController
- `/reservations` → ReservationController
- `/auth` → AuthController

### Frontend
- Proxy : `/api` → `http://localhost:8080/api`
- `/` → Liste spectacles
- `/spectacle/:id` → Détail
- `/login` → Connexion
- `/register` → Inscription
- `/cart` → Panier
- `/confirmation` → Confirmation
- `/statistics` → Statistiques

## Parcours utilisateur

Inscription → Connexion (JWT token) → Navigation spectacles → Ajout panier → Paiement → Confirmation e-billets.

## Dépannage

### Backend ne démarre pas

```powershell
Get-Service -Name postgresql*
netstat -ano | findstr :8080
```

### Encodage UTF-8

Vérifier `application.yml` : `connection-init-sql: "SET client_encoding TO 'UTF8'"`. Recréer la base en UTF-8 si nécessaire.

### Erreur JWT (401 Unauthorized)

Vérifier token dans localStorage. Vérifier header `Authorization: Bearer <token>`. Token expire après 24h, se reconnecter.

### Frontend non connecté au backend

Vérifier backend : http://localhost:8080/api/spectacles. Vérifier console navigateur (F12) pour erreurs CORS. Vérifier proxy Vite dans `vite.config.js`.

### Erreur compilation

```powershell
cd backend
mvn clean compile

cd frontend
npm run build
```

## Configuration CORS

Backend autorise :
- `http://localhost:3000`
- `http://127.0.0.1:3000`
- Méthodes HTTP : GET, POST, PUT, DELETE

