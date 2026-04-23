# 🎯 PAYMENT-SERVICE - CODE SOURCE COMPLET

Ce fichier contient tout le code source à créer pour le payment-service.

---

## 📁 FICHIERS RESTANTS À CRÉER

### 1. **PayPalConfig.java** (Configuration PayPal)

**Chemin** : `src/main/java/com/theatre/payment/config/PayPalConfig.java`

```java
package com.theatre.payment.config;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalConfig {
    
    @Value("${paypal.client.id}")
    private String clientId;
    
    @Value("${paypal.client.secret}")
    private String clientSecret;
    
    @Value("${paypal.mode}")
    private String mode;
    
    @Bean
    public PayPalHttpClient payPalHttpClient() {
        PayPalEnvironment environment;
        
        if ("live".equalsIgnoreCase(mode)) {
            environment = new PayPalEnvironment.Live(clientId, clientSecret);
        } else {
            environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
        }
        
        return new PayPalHttpClient(environment);
    }
}
```

---

### 2. **SecurityConfig.java** (Sécurité)

**Chemin** : `src/main/java/com/theatre/payment/config/SecurityConfig.java`

```java
package com.theatre.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and()
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        
        return http.build();
    }
}
```

---

### 3. **PaymentRepository.java** (Repository)

**Chemin** : `src/main/java/com/theatre/payment/repository/PaymentRepository.java`

```java
package com.theatre.payment.repository;

import com.theatre.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaypalOrderId(String paypalOrderId);
    Optional<Payment> findByReservationId(Long reservationId);
}
```

---

### 4. **PayPalService.java** (Service Principal - SIMPLIFIÉ)

**Chemin** : `src/main/java/com/theatre/payment/service/PayPalService.java`

```java
package com.theatre.payment.service;

import com.theatre.payment.dto.PaymentRequest;
import com.theatre.payment.dto.PaymentResponse;
import com.theatre.payment.model.Payment;
import com.theatre.payment.model.PaymentStatus;
import com.theatre.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayPalService {
    
    private final PaymentRepository paymentRepository;
    
    /**
     * VERSION SIMPLIFIÉE : Créer un paiement (simulation)
     * Pour la VRAIE intégration PayPal, voir PAYPAL_SETUP_GUIDE.md
     */
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        try {
            log.info("Création d'un paiement pour la réservation {}", request.getReservationId());
            
            // SIMULATION : Génère un ID PayPal fictif
            String mockPayPalOrderId = "MOCK-ORDER-" + UUID.randomUUID().toString().substring(0, 8);
            String mockApprovalUrl = "https://www.sandbox.paypal.com/checkoutnow?token=" + mockPayPalOrderId;
            
            // Sauvegarder dans la base
            Payment payment = Payment.builder()
                    .reservationId(request.getReservationId())
                    .paypalOrderId(mockPayPalOrderId)
                    .amount(request.getAmount())
                    .currency(request.getCurrency() != null ? request.getCurrency() : "EUR")
                    .status(PaymentStatus.CREATED)
                    .approvalUrl(mockApprovalUrl)
                    .build();
            
            payment = paymentRepository.save(payment);
            
            log.info("Paiement créé avec succès : {}", mockPayPalOrderId);
            
            return PaymentResponse.builder()
                    .id(payment.getId())
                    .reservationId(payment.getReservationId())
                    .paypalOrderId(mockPayPalOrderId)
                    .amount(payment.getAmount())
                    .currency(payment.getCurrency())
                    .status(PaymentStatus.CREATED)
                    .approvalUrl(mockApprovalUrl)
                    .build();
                    
        } catch (Exception e) {
            log.error("Erreur lors de la création du paiement", e);
            throw new RuntimeException("Erreur PayPal : " + e.getMessage());
        }
    }
    
    /**
     * Capturer un paiement approuvé
     */
    @Transactional
    public PaymentResponse capturePayment(String paypalOrderId) {
        log.info("Capture du paiement : {}", paypalOrderId);
        
        Payment payment = paymentRepository.findByPaypalOrderId(paypalOrderId)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        
        // SIMULATION : Marquer comme complété
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaypalCaptureId("CAPTURE-" + UUID.randomUUID().toString().substring(0, 8));
        payment = paymentRepository.save(payment);
        
        return PaymentResponse.builder()
                .id(payment.getId())
                .reservationId(payment.getReservationId())
                .paypalOrderId(payment.getPaypalOrderId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .build();
    }
}
```

---

### 5. **PaymentController.java** (Contrôleur REST)

**Chemin** : `src/main/java/com/theatre/payment/controller/PaymentController.java`

```java
package com.theatre.payment.controller;

import com.theatre.payment.dto.PaymentRequest;
import com.theatre.payment.dto.PaymentResponse;
import com.theatre.payment.service.PayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PaymentController {
    
    private final PayPalService payPalService;
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Payment Service OK - PayPal Sandbox Mode");
    }
    
    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = payPalService.createPayment(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/capture/{paypalOrderId}")
    public ResponseEntity<PaymentResponse> capturePayment(@PathVariable String paypalOrderId) {
        PaymentResponse response = payPalService.capturePayment(paypalOrderId);
        return ResponseEntity.ok(response);
    }
}
```

---

## 🚀 COMMENT OBTENIR TES VRAIS CREDENTIALS PAYPAL

### Étape 1 : Créer un compte PayPal Developer

1. Va sur **https://developer.paypal.com/**
2. Clique sur **"Log in to Dashboard"** (en haut à droite)
3. Si tu n'as pas de compte :
   - Clique sur **"Sign Up"**
   - Crée un compte avec ton email
   - Confirme ton email

### Étape 2 : Créer une App Sandbox

1. Une fois connecté, va dans **"Apps & Credentials"**
2. Assure-toi d'être en mode **"Sandbox"** (toggle en haut)
3. Clique sur **"Create App"**
4. Remplis :
   - **App Name** : `Theatre Payment`
   - **Merchant** : Sélectionne ton compte sandbox
5. Clique sur **"Create App"**

### Étape 3 : Copier les Credentials

Tu verras maintenant :
- **Client ID** : Une longue chaîne (exemple : `AXXXXxxxxxxx...`)
- **Secret** : Clique sur **"Show"** pour l'afficher

📋 **COPIE CES VALEURS** et mets-les dans `application.yml` :

```yaml
paypal:
  mode: sandbox
  client:
    id: TON_CLIENT_ID_ICI
    secret: TON_SECRET_ICI
```

### Étape 4 : Tester avec un compte Buyer

1. Va dans **"Sandbox" > "Accounts"**
2. Tu verras des comptes test (Personal et Business)
3. Note l'email et le mot de passe d'un compte **Personal**
4. Utilise-le pour approuver les paiements de test

---

## ✅ CHECKLIST FINALE

- [ ] Tous les fichiers Java créés
- [ ] `application.yml` configuré
- [ ] Credentials PayPal Sandbox obtenus
- [ ] `START_PAYMENT_SERVICE.bat` créé
- [ ] Service compilé : `mvn clean compile`
- [ ] Service démarré : `mvn spring-boot:run`
- [ ] Test santé : `GET http://localhost:8082/payment/health`
- [ ] Test création paiement : `POST http://localhost:8082/payment/create`

---

**🎉 Une fois ces étapes terminées, ton microservice de paiement sera opérationnel !**

Pour l'intégration complète avec PayPal (appels API réels), consulte la documentation officielle :
https://developer.paypal.com/docs/checkout/
