package com.theatre.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservice d'authentification
 * Responsabilité : Login et vérification de tokens JWT uniquement
 */
@SpringBootApplication
public class AuthServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   Auth Service - Démarré !                 ║");
        System.out.println("║   API: http://localhost:8081/auth          ║");
        System.out.println("║   Swagger: http://localhost:8081/swagger-ui║");
        System.out.println("╚════════════════════════════════════════════╝\n");
    }
}
