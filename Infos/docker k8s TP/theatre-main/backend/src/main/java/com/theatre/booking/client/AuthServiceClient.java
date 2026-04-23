package com.theatre.booking.client;

import com.theatre.booking.dto.request.LoginRequest;
import com.theatre.booking.dto.response.AuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Client Feign pour communiquer avec le microservice d'authentification
 * Ce client permet au booking-service d'appeler auth-service
 */
@FeignClient(name = "auth-service", url = "${auth.service.url}")
public interface AuthServiceClient {
    
    /**
     * Appelle le endpoint de login du microservice auth
     */
    @PostMapping("/login")
    AuthResponse login(@RequestBody LoginRequest request);
    
    /**
     * Vérifie la validité d'un token JWT via le microservice auth
     */
    @GetMapping("/verify")
    String verify(@RequestHeader("Authorization") String authHeader);
}
