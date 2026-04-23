# Guide complet pour visualiser les transactions PayPal Sandbox

## 🌐 Méthode 1: Interface Web PayPal Sandbox (LA PLUS SIMPLE)

### Étapes:
1. Allez sur: **https://www.sandbox.paypal.com**
2. Connectez-vous avec l'un de vos comptes test:
   - **Acheteur**: `sb-buyer47test@personal.example.com` / `TestBuyer123!`
   - **Marchand**: `sb-merchant47test@business.example.com` / `TestMerchant123!`
3. Une fois connecté, vous verrez toutes les transactions comme sur un vrai compte PayPal

### Avantages:
✅ Interface graphique complète
✅ Historique détaillé
✅ Téléchargement de relevés
✅ Voir les détails de chaque transaction

---

## 👨‍💻 Méthode 2: PayPal Developer Dashboard

### Étapes:
1. Allez sur: **https://developer.paypal.com**
2. Connectez-vous avec votre compte développeur
3. Allez dans **"Sandbox"** > **"Accounts"**
4. Cliquez sur l'icône "..." à côté d'un compte test
5. Sélectionnez **"View/Edit Account"** ou **"View transactions"**

### Fonctionnalités:
- Vue d'ensemble de tous vos comptes test
- Recherche de transactions
- Gestion des comptes sandbox
- Logs d'API calls

---

## 💻 Méthode 3: Scripts Automatisés (Terminal)

### Script PowerShell (RECOMMANDÉ pour Windows):

```powershell
# Exécuter depuis le terminal:
cd payment-service
.\CHECK_PAYPAL_TRANSACTIONS.ps1

# Pour voir plus de détails:
.\CHECK_PAYPAL_TRANSACTIONS.ps1 -Detailed

# Pour les 7 derniers jours seulement:
.\CHECK_PAYPAL_TRANSACTIONS.ps1 -Days 7
```

### Script Batch:
```batch
cd payment-service
CHECK_PAYPAL_TRANSACTIONS.bat
```

**⚠️ IMPORTANT**: Remplacez les identifiants dans les scripts par vos vrais identifiants PayPal Sandbox !

---

## 🔧 Méthode 4: Directement via curl (Terminal)

### 1. Obtenir un token d'accès:
```bash
curl -X POST "https://api-m.sandbox.paypal.com/v1/oauth2/token" \
  -H "Accept: application/json" \
  -H "Accept-Language: en_US" \
  -u "CLIENT_ID:SECRET" \
  -d "grant_type=client_credentials"
```

### 2. Lister les transactions:
```bash
curl -X GET "https://api-m.sandbox.paypal.com/v1/reporting/transactions?start_date=2025-10-01T00:00:00Z&end_date=2025-10-31T23:59:59Z&fields=all" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 3. Voir un ordre spécifique:
```bash
curl -X GET "https://api-m.sandbox.paypal.com/v2/checkout/orders/ORDER_ID" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

## 📊 Méthode 5: Via votre base de données locale

Votre `payment-service` enregistre toutes les transactions dans PostgreSQL:

```sql
-- Connectez-vous à PostgreSQL
psql -U postgres -d theatre_db

-- Voir toutes les transactions
SELECT * FROM payments ORDER BY created_at DESC;

-- Voir les statistiques
SELECT 
    status, 
    COUNT(*) as nombre,
    SUM(amount) as total
FROM payments 
GROUP BY status;

-- Voir les transactions récentes
SELECT 
    id,
    paypal_order_id,
    amount,
    currency,
    status,
    created_at
FROM payments 
WHERE created_at > NOW() - INTERVAL '7 days'
ORDER BY created_at DESC;
```

---

## 🎯 Récapitulatif des URLs importantes:

| Service | URL | Description |
|---------|-----|-------------|
| **Sandbox Web** | https://www.sandbox.paypal.com | Interface web complète |
| **Developer Dashboard** | https://developer.paypal.com | Gestion développeur |
| **API Sandbox** | https://api-m.sandbox.paypal.com | API REST |
| **API Live** | https://api-m.paypal.com | API production |
| **Logs Sandbox** | https://developer.paypal.com/dashboard/applications/sandbox | Logs d'API |

---

## 📝 Commandes rapides pour tester:

### Créer une transaction de test depuis votre terminal:
```powershell
# Créer un paiement
curl -X POST http://localhost:8082/payment/create `
  -H "Content-Type: application/json" `
  -d '{
    "reservationId": 1,
    "amount": 25.50,
    "currency": "EUR",
    "returnUrl": "http://localhost:3000/success",
    "cancelUrl": "http://localhost:3000/cancel"
  }'

# Vérifier dans votre BDD
psql -U postgres -d theatre_db -c "SELECT * FROM payments;"
```

---

## ⚙️ Configuration requise:

Pour utiliser les scripts automatisés, vous devez:

1. **Avoir un compte PayPal Developer**: https://developer.paypal.com
2. **Créer une application Sandbox**
3. **Récupérer vos identifiants** (Client ID + Secret)
4. **Remplacer les valeurs** dans `CHECK_PAYPAL_TRANSACTIONS.ps1`

---

## 🚀 Recommandation:

**La méthode la plus simple pour débuter:**
👉 Allez sur **https://www.sandbox.paypal.com** et connectez-vous avec vos comptes test !

C'est exactement comme le vrai PayPal, mais avec de l'argent fictif pour les tests.
