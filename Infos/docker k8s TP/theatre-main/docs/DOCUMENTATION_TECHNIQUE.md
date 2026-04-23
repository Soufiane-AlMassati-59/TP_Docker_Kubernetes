# Documentation Technique

## Architecture 3-Tiers

```
PRÉSENTATION
Web (React) | Mobile (React Native)
       ↓ HTTP/REST JSON
LOGIQUE MÉTIER
Spring Boot
Controllers | Security (JWT) | Services | Repositories
       ↓ JDBC
DONNÉES
PostgreSQL
Utilisateurs | Spectacles | Reservations
```

### Responsabilités

**Présentation** : Interface utilisateur, affichage, validation client, navigation.

**Logique Métier** : Traitement requêtes, validation métier, transactions, authentification, orchestration.

**Données** : Persistance, intégrité référentielle, optimisation requêtes, transactions ACID.

## MERISE

### MCD

```
UTILISATEUR (id_utilisateur, nom, prenom, email, mot_de_passe, role)
  1,n
  passe
  ↓
RESERVATION (id_reservation, date_reservation, quantite, montant_total, statut, code_confirm)
  1,n
  concerne
  ↓
SPECTACLE (id_spectacle, titre, description, date, heure, prix, places_dispo, image_url)
  1,n
  génère
  ↓
BILLET (id_billet, numero_place, code_qr, utilise)
```

### MLD (Modèle Logique de Données)

```sql
UTILISATEUR(#id_utilisateur, nom, prenom, email, mot_de_passe, role, date_inscription, actif)

SPECTACLE(#id_spectacle, titre, description, date_spectacle, heure_debut,
 prix_unitaire, image_url, places_totales, places_disponibles,
 categorie, actif, date_creation)

RESERVATION(#id_reservation, date_reservation, quantite, montant_total,
 statut, code_confirmation, =>id_utilisateur, =>id_spectacle)

BILLET(#id_billet, numero_place, code_qr, date_generation, utilise,
 date_utilisation, =>id_reservation)
```

### Règles de Gestion

1. Un utilisateur peut passer plusieurs réservations
2. Une réservation concerne un seul spectacle
3. Une réservation génère plusieurs billets (selon quantité)
4. Un spectacle peut avoir plusieurs réservations
5. Les places sont limitées par spectacle
6. Une réservation peut être annulée si le spectacle n'a pas eu lieu
7. Un billet ne peut être utilisé qu'une seule fois

## Diagrammes UML

### Diagramme de Classes (Simplifié)

```

 Utilisateur

 - id: Long
 - nom: String
 - prenom: String
 - email: String
 - motDePasse: String
 - role: Role
 - reservations: List

 + isAdmin(): boolean
 + getNomComplet():String

 1
 possède
 *

 Reservation

 - id: Long
 - dateReservation: Date
 - quantite: Integer
 - montantTotal: Decimal
 - statut: StatutRes
 - codeConfirmation: Str
 - utilisateur: User
 - spectacle: Spectacle
 - billets: List

 + calculerMontant()
 + confirmer()
 + annuler()
 + peutEtreAnnulee():bool

 * Spectacle
 pour
 1 - id: Long
 > - titre: String
 - description: String
 - dateSpectacle: Date
 - heureDebut: Time
 1 - prixUnitaire: Decimal

### MLD

```sql
utilisateurs (id_utilisateur PK, nom, prenom, email UNIQUE, mot_de_passe, date_inscription, actif, role)
spectacles (id_spectacle PK, titre, description, date_spectacle, heure_debut, duree_minutes, prix_unitaire, places_totales, places_disponibles, statut, image_url)
reservations (id_reservation PK, #id_utilisateur FK, #id_spectacle FK, nombre_places, montant_total, date_reservation, statut)
billets (id_billet PK, #id_reservation FK, numero_billet UNIQUE, date_emission, qr_code, statut)
```

## UML

Diagrammes UML complets disponibles dans `docs/uml/`

## Design Patterns

**Repository** : `SpectacleRepository extends JpaRepository` - Abstraction accès données

**Service Layer** : `@Service @Transactional` - Logique métier centralisée

**DTO** : `SpectacleResponse` - Contrôle données exposées, optimisation réseau

**Factory** : `BilletService.genererBillets()` - Création billets avec QR codes

**Singleton** : `JwtUtil @Component` - Instance unique JWT provider

**Context** : `CartContext` React - État global panier

## API REST

### Principes
- Stateless (JWT dans chaque requête)
- Ressources : `/spectacles`, `/reservations`
- Verbes HTTP : GET, POST, PUT, DELETE
- Codes : 200 (OK), 201 (Created), 400 (Bad Request), 401 (Unauthorized), 404 (Not Found)
- Format : JSON
- CORS configuré

### Endpoints

**Auth**
- POST `/api/auth/register` - Inscription (BCrypt)
- POST `/api/auth/login` - JWT token
- GET `/api/auth/profile` - Profil (Bearer token)

#### Spectacles
- `GET /api/spectacles` - Liste complète
- `GET /api/spectacles/{id}` - Détails
- `GET /api/spectacles/search?query=...` - Recherche

## Sécurité

### Authentification JWT (JSON Web Token)

1. **Login** : `POST /api/auth/login`
 - Entrée : email + password
 - Vérification : BCrypt compare avec hash en base
 - Sortie : JWT token signé avec HS256
 
2. **Token JWT Structure** :
 ```json
 {
   "header": {
     "alg": "HS256",
     "typ": "JWT"
   },
   "payload": {
     "sub": "user@example.com",
     "userId": 1,
     "role": "USER",
     "iat": 1698576000,
     "exp": 1698662400
   },
   "signature": "..."
 }
 ```

3. **Validation du token** :
 - Vérification de la signature
 - Vérification de l'expiration (24h)
 - Extraction des claims (userId, email, role)

4. **Protection des endpoints** :
 - Header requis : `Authorization: Bearer <token>`
 - Gestion automatique côté frontend via intercepteur
 - Redirection vers `/login` si 401 Unauthorized

### Hachage des mots de passe

- **Algorithme** : BCrypt avec salt automatique
- **Rounds** : 10 (recommandé pour production)
- **Exemple** :
```java
String hashedPassword = passwordEncoder.encode(plainPassword);
boolean matches = passwordEncoder.matches(plainPassword, hashedPassword);
```

### CORS (Cross-Origin Resource Sharing)

Configuration Spring Boot pour autoriser le frontend :
```java
@Configuration
public class CorsConfig {
 @Bean
 public CorsFilter corsFilter() {
 config.setAllowedOrigins(Arrays.asList(
 "http://localhost:3000",
 "http://localhost:5173",
 "http://localhost:4200"
 ));
 config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
 config.setAllowCredentials(true);
 }
}
```
 
### Protection contre les injections SQL

- **JPA/Hibernate** : Utilisation de requêtes préparées
- **Aucune concaténation SQL manuelle**
- Exemple sécurisé :
```java
@Query("SELECT s FROM Spectacle s WHERE s.titre LIKE %:query%")
List<Spectacle> searchByTitre(@Param("query") String query);
```

## Gestion du panier (Frontend)

### Architecture

- **React Context API** : État global partagé entre composants
- **localStorage** : Persistance des données (survit au rechargement)
- **Structure** :
```javascript
cart: [
 { spectacle: {...}, quantity: 2 },
 { spectacle: {...}, quantity: 1 }
]
```

### Fonctionnalités

1. **Ajout au panier** : Vérification des places disponibles
2. **Mise à jour quantité** : Contrôle des limites
3. **Suppression d'articles** : Mise à jour instantanée
4. **Calcul du total** : Prix × quantité pour chaque article
5. **Badge de notification** : Affichage du nombre d'articles dans le header
6. **Persistance** : Sauvegarde automatique dans localStorage

### Workflow de paiement

1. **Page panier** : Révision des articles
2. **Checkout modal** : Choix du mode de paiement (carte/PayPal)
3. **Simulation paiement** : Délai de 2 secondes
4. **Confirmation** : Affichage des e-billets avec QR codes
5. **Vidage du panier** : Après confirmation réussie
 - Sortie : JWT Token (Bearer)

2. **Utilisation** : Header `Authorization: Bearer <token>`

3. **Validation** : À chaque requête protégée

### Commandes

**Backend**
```bash
mvn clean install -DskipTests
mvn spring-boot:run
mvn test
mvn package
```

**Frontend**
```bash
npm install
npm run dev
npm run build
npm run preview
```