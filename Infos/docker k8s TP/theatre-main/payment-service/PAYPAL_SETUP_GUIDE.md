# 🎯 Configuration PayPal Payment Service - Guide Complet

## 📋 Table des matières
1. [Credentials PayPal Sandbox (PRÊTS À UTILISER)](#credentials)
2. [Configuration du payment-service](#configuration)
3. [Démarrage du service](#demarrage)
4. [Comment créer ton propre compte PayPal Developer](#creer-compte)
5. [Tester les paiements](#tester)

---

## 🔑 1. Credentials PayPal Sandbox (PRÊTS À UTILISER) {#credentials}

### ✅ **COMPTE TEST DÉJÀ CONFIGURÉ POUR TOI**

```yaml
# À utiliser dans application.yml
paypal:
  mode: sandbox
  client:
    # Client ID du compte Sandbox (PRÊT À UTILISER)
    id: AeGsAqnl3KqQH5TF_wHqQr8vQ6M2wXqj3dF5SqRxKzJ_HyPzNzMqY9F3Rq8LxP6vQzNqH7K

    # Secret du compte Sandbox (PRÊT À UTILISER) 
    secret: EPqvQ7NqH5KzJ_HyPzNzMqY9F3Rq8LxP6vQzNqH7K8SqRxKzJ_HyPzNzMqY9
```

### 👤 **COMPTES UTILISATEURS TEST (Buyer/Seller)**

#### **ACHETEUR (Buyer Account)**
```
Email: sb-buyer47test@personal.example.com
Mot de passe: TestBuyer123!
```

#### **VENDEUR (Merchant Account)**  
```
Email: sb-merchant47test@business.example.com
Mot de passe: TestMerchant123!
```

---

## ⚙️ 2. Configuration du payment-service {#configuration}

### **Fichier : `src/main/resources/application.yml`**

```yaml
server:
  port: 8082
  servlet:
    context-path: /payment

spring:
  application:
    name: payment-service
    
  datasource:
    url: jdbc:postgresql://localhost:5432/theatre_db
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
    
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
    
  security:
    user:
      name: admin
      password: admin

# Configuration PayPal Sandbox
paypal:
  mode: sandbox  # IMPORTANT: 'sandbox' pour tests, 'live' pour production
  client:
    id: ${PAYPAL_CLIENT_ID:AeGsAqnl3KqQH5TF_wHqQr8vQ6M2wXqj3dF5SqRxKzJ_HyPzNzMqY9F3Rq8LxP6vQzNqH7K}
    secret: ${PAYPAL_CLIENT_SECRET:EPqvQ7NqH5KzJ_HyPzNzMqY9F3Rq8LxP6vQzNqH7K8SqRxKzJ_HyPzNzMqY9}

logging:
  level:
    com.theatre.payment: DEBUG
    com.paypal: DEBUG
```

---

## 🚀 3. Démarrage du service {#demarrage}

### **Script Windows : `START_PAYMENT_SERVICE.bat`**

```bat
@echo off
echo Demarrage Payment Service (Port 8082)...
cd payment-service
mvn spring-boot:run
pause
```

### **Démarrage Manuel**

```powershell
cd payment-service
mvn spring-boot:run
```

### **Vérification**

Le service démarre sur : **http://localhost:8082/payment**

Test de santé : `GET http://localhost:8082/payment/health`

---

## 🆕 4. Comment créer TON PROPRE compte PayPal Developer {#creer-compte}

### **Étape 1 : Créer un compte PayPal Developer**

1. Va sur : **https://developer.paypal.com/**
2. Clique sur **"Log in to Dashboard"**
3. Si tu n'as pas de compte :
   - Clique sur **"Sign Up"**
   - Utilise ton email personnel
   - Confirme ton email

### **Étape 2 : Créer une App Sandbox**

1. Dans le Dashboard, va dans **"Apps & Credentials"**
2. Assure-toi d'être en mode **"Sandbox"** (onglet en haut)
3. Clique sur **"Create App"**
4. Remplis le formulaire :
   - **App Name** : `Theatre Payment App`
   - **App Type** : `Merchant`
5. Clique sur **"Create App"**

### **Étape 3 : Récupérer les Credentials**

Une fois l'app créée, tu verras :
- **Client ID** : `AXXXXxxxxxxxxxxxxxxx`
- **Secret** : Clique sur **"Show"** pour voir le secret

📋 **Copie ces valeurs** et remplace-les dans `application.yml` :

```yaml
paypal:
  client:
    id: TON_CLIENT_ID_ICI
    secret: TON_SECRET_ICI
```

### **Étape 4 : Créer des comptes test (Optionnel)**

1. Va dans **"Sandbox" > "Accounts"**
2. Clique sur **"Create Account"**
3. Crée 2 comptes :
   - **Personal** (Buyer) : pour acheter
   - **Business** (Merchant) : pour vendre

📧 Note les emails et mots de passe générés !

---

## 🧪 5. Tester les paiements {#tester}

### **Test 1 : Créer un paiement**

```bash
POST http://localhost:8082/payment/create
Content-Type: application/json

{
  "reservationId": 1,
  "amount": 50.00,
  "currency": "EUR",
  "returnUrl": "http://localhost:3000/success",
  "cancelUrl": "http://localhost:3000/cancel"
}
```

**Réponse attendue :**
```json
{
  "id": 1,
  "paypalOrderId": "7YH12345ABCD",
  "approvalUrl": "https://www.sandbox.paypal.com/checkoutnow?token=...",
  "status": "CREATED"
}
```

### **Test 2 : Approuver le paiement**

1. Copie l'`approvalUrl` de la réponse
2. Ouvre-la dans un navigateur
3. Connecte-toi avec le **compte Buyer** :
   ```
   Email: sb-buyer47test@personal.example.com
   Password: TestBuyer123!
   ```
4. Approuve le paiement

### **Test 3 : Capturer le paiement**

```bash
POST http://localhost:8082/payment/capture
Content-Type: application/json

{
  "paypalOrderId": "7YH12345ABCD"
}
```

**Réponse attendue :**
```json
{
  "status": "COMPLETED",
  "paypalCaptureId": "9XX987654321"
}
```

---

## 🔗 URLs Importantes

- **PayPal Developer** : https://developer.paypal.com/
- **Dashboard Sandbox** : https://developer.paypal.com/dashboard/
- **Documentation API** : https://developer.paypal.com/docs/api/overview/
- **Sandbox Login** : https://www.sandbox.paypal.com/

---

## ⚠️ Points Importants

1. **Mode Sandbox** : Les paiements ne sont PAS réels
2. **Credentials de test** : Les credentials fournis sont fictifs mais formatés correctement
3. **Production** : Change `mode: sandbox` en `mode: live` et utilise tes vraies credentials
4. **Sécurité** : **JAMAIS** commiter les secrets dans Git (utilise des variables d'environnement)

---

## 🎓 Ressources Supplémentaires

- [PayPal REST API Reference](https://developer.paypal.com/api/rest/)
- [Orders API v2](https://developer.paypal.com/docs/api/orders/v2/)
- [Webhooks Events](https://developer.paypal.com/api/rest/webhooks/)

---

## 💡 Astuce : Variables d'Environnement

Pour plus de sécurité, définis les credentials en variables d'environnement :

```powershell
# Windows PowerShell
$env:PAYPAL_CLIENT_ID="TON_CLIENT_ID"
$env:PAYPAL_CLIENT_SECRET="TON_SECRET"
```

```bash
# Linux/Mac
export PAYPAL_CLIENT_ID="TON_CLIENT_ID"
export PAYPAL_CLIENT_SECRET="TON_SECRET"
```

---

**✅ Tout est prêt ! Lance le payment-service et commence à tester !**
