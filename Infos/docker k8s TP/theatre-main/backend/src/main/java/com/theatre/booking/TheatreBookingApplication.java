package com.theatre.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Application principale de la plateforme de billetterie
 * Architecture Microservices:
 * - booking-service (ce service) : Gestion des réservations, spectacles, et création de compte
 * - auth-service (microservice séparé) : Authentification (login)
 * 
 * @author Équipe DevOps M1
 * @version 2.0.0 - Architecture Microservices
 */
@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
public class TheatreBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheatreBookingApplication.class, args);
        System.out.println("\n" +
                "╔══════════════════════════════════════════════╗\n" +
                "║   Plateforme de Billetterie - Démarrée !     ║\n" +
                "║   API: http://localhost:8080/api             ║\n" +
                "║   Swagger: http://localhost:8080/swagger-ui  ║\n" +
                "╚══════════════════════════════════════════════╝\n");
    }
}
