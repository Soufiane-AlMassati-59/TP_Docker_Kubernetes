# Plateforme Billetterie Théâtre

Projet M1 DevOps - Architecture applicative

## Fonctionnalités

Gestion spectacles (consultation, recherche, détails), statistiques temps réel, recherche avancée, interface responsive, API REST Swagger, base PostgreSQL (vues/triggers).

## Démarrage

### Windows
- `START_ALL.bat` - Backend + Frontend
- `START_BACKEND.bat` - Backend seul
- `START_FRONTEND.bat` - Frontend seul
- `install-dependencies.bat` - Installation dépendances

### Linux/Mac
```bash
./start-backend.sh
```

### Manuel

**Backend**
```bash
cd backend
mvn spring-boot:run
```

**Frontend**
```bash
cd frontend
npm ci
npm run dev
```

### Accès

| Service | URL |
|---------|-----|
| Frontend | http://localhost:3000 |
| API | http://localhost:8080/api |
| Swagger | http://localhost:8080/swagger-ui/index.html |
| Test API | TEST_API.html |

## Structure

```
theatre-booking/
├── backend/          # Spring Boot API
├── frontend/         # React interface
├── docs/             # Documentation (ARCHITECTURE, DEMARRAGE, etc.)
│   └── uml/          # Diagrammes Mermaid
├── architecture_bdd.sql
├── START_*.bat       # Scripts Windows
├── start-backend.sh  # Script Linux/Mac
└── TEST_API.html     # Test endpoints
```

## Technologies

**Backend** : Spring Boot 3.2.0, Spring Data JPA, Spring Security, PostgreSQL 18, Hibernate, Lombok, SpringDoc OpenAPI

**Frontend** : React 18, Vite 5, React Router v6, CSS3

## API Endpoints

### Spectacles
- GET `/api/spectacles` - Liste
- GET `/api/spectacles/{id}` - Détails
- GET `/api/spectacles/upcoming` - À venir
- GET `/api/spectacles/search?query=...` - Recherche

### Statistiques
- GET `/api/statistics/global` - Globales
- GET `/api/statistics/popular?limit=10` - Populaires

### Réservations
- POST `/api/reservations` - Créer
- GET `/api/reservations/mes-reservations` - Liste utilisateur

## Design Patterns

MVC, Repository Pattern, Service Layer, DTO Pattern, Builder Pattern (Lombok), Dependency Injection (Spring IoC).

## Documentation

Dossier `docs/` : README.md (index), ARCHITECTURE.md (système complet), DEMARRAGE.md (guide détaillé), DOCUMENTATION_TECHNIQUE.md, LIVRABLES.md, RAPPORT_SYNTHESE.md, uml/ (diagrammes Mermaid).

## Tests

**Backend**
```bash
cd backend
mvn test
mvn clean install -DskipTests
```

**Frontend**
```bash
cd frontend
npm test
```

**Endpoints** : Ouvrir TEST_API.html pour interface interactive.

## Évolutions Futures

Authentification JWT complète, paiement en ligne, billets PDF QR code, notifications email, app mobile React Native, tests unitaires/intégration, CI/CD GitHub Actions, déploiement Docker/Kubernetes, recommandations, chat support, places numérotées, statistiques graphiques.

## Diagrammes UML

Disponibles format Mermaid dans `docs/uml/` : diagramme_classes_complet.mmd, diagramme_sequence_complet.mmd, diagramme_etats.mmd.

## Sécurité

JWT, BCrypt, validation entrées, CORS, autorisation rôles (USER/ADMIN).

Licence : Usage éducatif M1 DevOps

Pour plus d'informations : [Documentation complète](docs/)
