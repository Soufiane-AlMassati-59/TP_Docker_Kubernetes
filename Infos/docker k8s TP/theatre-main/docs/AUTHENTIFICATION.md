# Guide Authentification

## Implémentation

### Backend

**JwtUtil** (`security/JwtUtil.java`) : Génération/validation tokens JWT, extraction infos utilisateur.

**UtilisateurService** : Intégration JwtUtil pour login/register, hachage BCrypt, gestion erreurs.

**AuthController** : POST `/api/auth/register`, POST `/api/auth/login`, GET `/api/auth/verify`.

**Configuration** : JWT secret dans `application.yml`, expiration 24h, CORS frontend.

### Frontend

**Composants** : Login.jsx, Register.jsx, Header.jsx (état auth), Auth.css (styles modernes).

**Service API** : Inclusion auto token JWT, gestion expiration (redirection /login), messages erreur.

**Routes** : `/login`, `/register`, Header avec Connexion/Inscription ou Nom/Déconnexion.

## Utilisation

### Démarrage

**Backend**
```bash
cd backend
mvn spring-boot:run
```
http://localhost:8080

**Frontend**
```bash
cd frontend
npm install
npm run dev
```
http://localhost:3000

### Test

**Inscription** : Formulaire → Nom, Prénom, Email, Mot de passe → Auto-connecté après création.

**Connexion** : Email + Mot de passe → Redirigé vers accueil.

**Déconnexion** : Bouton Header → Token supprimé.

## Endpoints

**Register**
```
POST /api/auth/register
Body: {"nom":"Dupont","prenom":"Jean","email":"jean@exemple.com","motDePasse":"test123456"}
Response: {"token":"eyJ...","type":"Bearer","id":1,"email":"jean@exemple.com","nom":"Dupont","prenom":"Jean","role":"USER"}
```

**Login**
```
POST /api/auth/login
Body: {"email":"jean@exemple.com","motDePasse":"test123456"}
Response: {"token":"eyJ...","type":"Bearer","id":1,"email":"jean@exemple.com","nom":"Dupont","prenom":"Jean","role":"USER"}
```
  "prenom": "Jean",
  "role": "USER"
}
```

```
GET /api/auth/verify
Headers: Authorization: Bearer <token>

Response: "Token valide"
```

## Sécurité

### Token JWT
- **Algorithme**: HS256 (HMAC with SHA-256)
- **Durée de vie**: 24 heures
- **Contenu**: 
  - `userId`: ID de l'utilisateur
  - `email`: Email de l'utilisateur
  - `role`: Rôle (USER/ADMIN)

### Mot de passe
- **Hachage**: BCrypt avec facteur 10
- **Validation**: Minimum 6 caractères
- Stocké de manière sécurisée dans la base de données

### LocalStorage
- Token JWT stocké dans `localStorage.token`
- Infos utilisateur dans `localStorage.user`
- Supprimés à la déconnexion

## Structure de la base de données

La table `utilisateurs` existe déjà avec:
- `id_utilisateur` (SERIAL PRIMARY KEY)
- `nom` (VARCHAR 100)
- `prenom` (VARCHAR 100)
- `email` (VARCHAR unique)
- `mot_de_passe` (TEXT - BCrypt hash)
- `role` (ENUM: USER, ADMIN)
- `date_inscription` (TIMESTAMP)
- `actif` (BOOLEAN)

## Design

- Gradient violet moderne (même que le header existant)
- Formulaires avec validation en temps réel
- Messages d'erreur clairs
- Animations fluides
- Responsive (mobile-friendly)

## Prochaines étapes possibles

1. **Protection des routes**
   - Créer un composant `ProtectedRoute`
   - Rediriger vers `/login` si non authentifié
   - Protéger les pages de réservation

2. **Rôles et permissions**
   - Page d'administration pour les ADMIN
   - Gestion des utilisateurs
   - Statistiques avancées

3. **Profil utilisateur**
   - Page de profil (`/profile`)
   - Historique des réservations
   - Modification des informations

4. **Forgot password**
   - Page de réinitialisation
   - Email de récupération
   - Token temporaire

5. **Remember me**
   - Option "Se souvenir de moi"
   - Refresh token pour prolonger la session

## Résolution des problèmes

### Le backend ne démarre pas
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### Le frontend ne trouve pas l'API
- Vérifiez que le backend tourne sur port 8080
- Vérifiez le proxy dans `vite.config.js` ou `package.json`
- Vérifiez les CORS dans `application.yml`

### Token invalide ou expiré
- Le token expire après 24h
- Reconnectez-vous
- Vérifiez que le secret JWT est le même côté backend

### Erreur 500 lors du login
- Vérifiez que la base de données est accessible
- Vérifiez que l'utilisateur existe
- Consultez les logs du backend
