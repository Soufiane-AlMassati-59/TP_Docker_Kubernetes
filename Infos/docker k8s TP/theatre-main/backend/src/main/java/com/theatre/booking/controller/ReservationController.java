package com.theatre.booking.controller;

import com.theatre.booking.dto.request.ReservationRequest;
import com.theatre.booking.dto.response.ReservationResponse;
import com.theatre.booking.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour les réservations
 * Pattern: MVC Controller
 */
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservationController {
    
    private final ReservationService reservationService;
    
    /**
     * Créer une nouvelle réservation
     */
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        ReservationResponse reservation = reservationService.createReservation(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }
    
    /**
     * Récupérer toutes les réservations de l'utilisateur connecté
     */
    @GetMapping("/mes-reservations")
    public ResponseEntity<List<ReservationResponse>> getMesReservations(Authentication authentication) {
        String email = authentication.getName();
        List<ReservationResponse> reservations = reservationService.getReservationsByUtilisateur(email);
        return ResponseEntity.ok(reservations);
    }
    
    /**
     * Récupérer une réservation par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservationById(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        ReservationResponse reservation = reservationService.getReservationById(id);
        
        // Vérifier que la réservation appartient à l'utilisateur
        if (!reservation.getUtilisateurEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(reservation);
    }
    
    /**
     * Annuler une réservation
     */
    @PostMapping("/{id}/annuler")
    public ResponseEntity<ReservationResponse> annulerReservation(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        ReservationResponse reservation = reservationService.annulerReservation(id, email);
        return ResponseEntity.ok(reservation);
    }
    
    /**
     * Récupérer toutes les réservations (Admin uniquement)
     */
    @GetMapping("/admin/all")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }
}
