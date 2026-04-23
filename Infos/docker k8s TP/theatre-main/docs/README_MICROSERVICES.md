# Architecture Microservices - Plateforme de Billetterie Théâtre

## 📋 Vue d'ensemble

L'application est maintenant basée sur une **architecture microservices** avec deux services indépendants :

### 🔐 **auth-service** (Port 8081)
Microservice d'authentification
- **Responsabilités** : Login et vérification JWT uniquement
- **Endpoints** :
  - `POST /auth/login` - Connexion utilisateur
  - `GET /auth/verify` - Vérification token JWT
  - `GET /auth/health` - Health check

### 🎭 **booking-service** (Port 8080)
Service principal de gestion métier
- **Responsabilités** : Création de compte, gestion des spectacles, réservations et billets
- **Endpoints** :
  - `POST /api/auth/register` - Création de compte (reste dans ce service)
  - `POST /api/auth/login` - Délégué à auth-service via Feign Client
  - `GET /api/auth/verify` - Délégué à auth-service via Feign Client
  - `/api/spectacles/*` - Gestion des spectacles
  - `/api/reservations/*` - Gestion des réservations
  - `/api/statistics/*` - Statistiques

---

## 🚀 Démarrage rapide

### Option 1 : Tout démarrer en une fois
```powershell
.\START_MICROSERVICES.bat
```

### Option 2 : Démarrage manuel

#### 1. Démarrer auth-service (port 8081)
```powershell
cd auth-service
mvn spring-boot:run
```

#### 2. Démarrer booking-service (port 8080)
```powershell
cd backend
mvn spring-boot:run
```

#### 3. Démarrer le frontend (port 3000)
```powershell
cd frontend
npm install
npm run dev
```

---

## 🏗️ Architecture technique

### Communication inter-services

Le **booking-service** communique avec **auth-service** via **Spring Cloud OpenFeign** :

```java
@FeignClient(name = "auth-service", url = "http://localhost:8081/auth")
public interface AuthServiceClient {
    @PostMapping("/login")
    AuthResponse login(@RequestBody LoginRequest request);
    
    @GetMapping("/verify")
    String verify(@RequestHeader("Authorization") String authHeader);
}
```

### Flow d'authentification

#### Création de compte (Register)
```
Frontend → POST /api/auth/register → Booking Service
                                      ↓
                                  Crée utilisateur
                                      ↓
                                  Génère JWT
                                      ↓
                                  Retourne token
```

#### Connexion (Login)
```
Frontend → POST /api/auth/login → Booking Service
                                      ↓
                                  Feign Client
                                      ↓
                              Auth Service (8081)
                                      ↓
                                  Vérifie credentials
                                      ↓
                                  Génère JWT
                                      ↓
                                  Retourne token
```

---

## 📦 Structure des projets

```
theatre/
├── auth-service/              # Microservice d'authentification
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/theatre/auth/
│       │   ├── AuthServiceApplication.java
│       │   ├── controller/AuthController.java
│       │   ├── service/AuthService.java
│       │   ├── repository/UtilisateurRepository.java
│       │   ├── model/Utilisateur.java
│       │   ├── security/JwtUtil.java
│       │   └── config/
│       └── resources/
│           └── application.yml (port 8081)
│
├── backend/                   # Booking service (service principal)
│   ├── pom.xml (avec Spring Cloud OpenFeign)
│   └── src/main/
│       ├── java/com/theatre/booking/
│       │   ├── client/AuthServiceClient.java  # Feign Client
│       │   ├── controller/
│       │   │   ├── AuthController.java        # Délègue login à auth-service
│       │   │   ├── ReservationController.java
│       │   │   └── SpectacleController.java
│       │   └── service/UtilisateurService.java # Gère uniquement register
│       └── resources/
│           └── application.yml (port 8080 + URL auth-service)
│
├── frontend/                  # Interface React
│   └── src/services/api.js    # Appelle toujours /api/auth/* (booking-service)
│
├── START_AUTH_SERVICE.bat     # Démarre auth-service seul
├── START_MICROSERVICES.bat    # Démarre les 2 services
└── README_MICROSERVICES.md    # Ce fichier
```

---

## 🔧 Configuration

### auth-service (`auth-service/src/main/resources/application.yml`)
```yaml
server:
  port: 8081
  servlet:
    context-path: /auth

spring:
  application:
    name: auth-service
  datasource:
    url: jdbc:postgresql://localhost:5432/theatre_db
```

### booking-service (`backend/src/main/resources/application.yml`)
```yaml
server:
  port: 8080
  servlet:
    context-path: /api

auth:
  service:
    url: http://localhost:8081/auth  # URL du microservice auth
```

---

## 🧪 Tester l'architecture

### 1. Health check auth-service
```powershell
curl http://localhost:8081/auth/health
```

### 2. Créer un compte (booking-service)
```powershell
curl -X POST http://localhost:8080/api/auth/register `
  -H "Content-Type: application/json" `
  -d '{
    "nom": "Test",
    "prenom": "User",
    "email": "test@example.com",
    "motDePasse": "password123"
  }'
```

### 3. Se connecter (délégué à auth-service)
```powershell
curl -X POST http://localhost:8080/api/auth/login `
  -H "Content-Type: application/json" `
  -d '{
    "email": "test@example.com",
    "motDePasse": "password123"
  }'
```

---

## 📊 Avantages de l'architecture microservices

✅ **Séparation des responsabilités**
- Auth-service : Authentification uniquement
- Booking-service : Logique métier et création de compte

✅ **Scalabilité indépendante**
- Chaque service peut être déployé et mis à l'échelle séparément

✅ **Résilience**
- Si auth-service tombe, le booking-service peut continuer à fonctionner (hors login)

✅ **Évolution indépendante**
- Possibilité de changer la technologie d'auth sans toucher au booking

✅ **Sécurité**
- Les credentials sont vérifiés dans un service dédié et isolé

---

## 🛠️ Dépendances ajoutées

### booking-service (`pom.xml`)
```xml
<!-- Spring Cloud OpenFeign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2023.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## 🔒 Sécurité

- Les deux services utilisent le **même secret JWT** pour générer et valider les tokens
- Les mots de passe sont hashés avec **BCrypt**
- La communication entre services utilise **HTTP** (en production, utiliser HTTPS)
- CORS configuré pour autoriser le frontend

---

## 📝 Notes importantes

1. **Base de données partagée** : Les deux services partagent la même DB PostgreSQL (table `utilisateur`)
2. **JWT synchronisé** : Les deux services utilisent la même clé secrète JWT
3. **Frontend transparent** : Le frontend appelle toujours `/api/auth/*` (booking-service), qui redirige vers auth-service si nécessaire

---

## 🎯 Prochaines étapes possibles

- [ ] Ajouter un API Gateway (Spring Cloud Gateway)
- [ ] Service Discovery avec Eureka
- [ ] Circuit Breaker avec Resilience4j
- [ ] Séparation des bases de données (une DB par service)
- [ ] Monitoring avec Spring Boot Admin
- [ ] Distributed tracing avec Sleuth + Zipkin

---

## 🐛 Troubleshooting

### Auth-service ne démarre pas
- Vérifier que le port 8081 est libre : `netstat -ano | findstr ":8081"`
- Vérifier la connexion PostgreSQL

### Booking-service ne peut pas joindre auth-service
- Vérifier que auth-service est démarré
- Vérifier l'URL dans `application.yml` : `auth.service.url=http://localhost:8081/auth`
- Tester manuellement : `curl http://localhost:8081/auth/health`

### Erreur Feign Client
```
Feign.RetryableException: Connection refused
```
→ Auth-service n'est pas démarré ou l'URL est incorrecte

---

## 📞 Support

Pour toute question sur l'architecture microservices, consulter :
- [Spring Cloud OpenFeign Documentation](https://spring.io/projects/spring-cloud-openfeign)
- [Microservices Pattern](https://microservices.io/)

---

**Version** : 2.0.0 - Architecture Microservices  
**Date** : Octobre 2025  
**Équipe** : DevOps M1
