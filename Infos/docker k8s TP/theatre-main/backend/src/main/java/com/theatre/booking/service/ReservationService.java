package com.theatre.booking.service;

import com.theatre.booking.dto.request.ReservationRequest;
import com.theatre.booking.dto.response.BilletResponse;
import com.theatre.booking.dto.response.ReservationResponse;
import com.theatre.booking.model.*;
import com.theatre.booking.repository.ReservationRepository;
import com.theatre.booking.repository.SpectacleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des réservations
 * Pattern: Service Layer Pattern
 * Pattern: Factory Pattern (pour la création des billets)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final SpectacleRepository spectacleRepository;
    private final BilletService billetService;
    private final UtilisateurService utilisateurService;
    
    public ReservationResponse creerReservation(ReservationRequest request, Long utilisateurId) {
        // Récupérer le spectacle
        Spectacle spectacle = spectacleRepository.findById(request.getSpectacleId())
                .orElseThrow(() -> new RuntimeException("Spectacle non trouvé"));
        
        // Vérifier la disponibilité
        if (!spectacle.verifierDisponibilite(request.getQuantite())) {
            throw new RuntimeException("Places insuffisantes pour ce spectacle");
        }
        
        // Calculer le montant total
        BigDecimal montantTotal = spectacle.getPrixUnitaire()
                .multiply(BigDecimal.valueOf(request.getQuantite()));
        
        // Créer la réservation
        Reservation reservation = Reservation.builder()
                .utilisateur(Utilisateur.builder().id(utilisateurId).build())
                .spectacle(spectacle)
                .quantite(request.getQuantite())
                .montantTotal(montantTotal)
                .statut(StatutReservation.CONFIRMEE)
                .codeConfirmation(genererCodeConfirmation())
                .build();
        
        // Réserver les places
        try {
            spectacle.reserverPlaces(request.getQuantite());
            spectacleRepository.save(spectacle);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la réservation: " + e.getMessage());
        }
        
        // Sauvegarder la réservation
        Reservation saved = reservationRepository.save(reservation);
        
        // Générer les billets (Pattern Factory)
        List<Billet> billets = billetService.genererBillets(saved, request.getQuantite());
        saved.setBillets(billets);
        
        return convertToResponse(saved);
    }
    
    public void annulerReservation(Long reservationId, Long utilisateurId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        // Vérifier que la réservation appartient à l'utilisateur
        if (!reservation.getUtilisateur().getId().equals(utilisateurId)) {
            throw new RuntimeException("Cette réservation ne vous appartient pas");
        }
        
        if (!reservation.peutEtreAnnulee()) {
            throw new RuntimeException("Cette réservation ne peut plus être annulée");
        }
        
        try {
            reservation.annuler();
            reservationRepository.save(reservation);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'annulation: " + e.getMessage());
        }
    }
    
    public List<ReservationResponse> getReservationsUtilisateur(Long utilisateurId) {
        return reservationRepository.findByUtilisateurIdOrderByDateReservationDesc(utilisateurId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public ReservationResponse getReservation(Long id, Long utilisateurId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        if (!reservation.getUtilisateur().getId().equals(utilisateurId)) {
            throw new RuntimeException("Cette réservation ne vous appartient pas");
        }
        
        return convertToResponse(reservation);
    }
    
    private String genererCodeConfirmation() {
        return "CONF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    // Méthodes pour les contrôleurs
    public ReservationResponse createReservation(ReservationRequest request, String email) {
        Utilisateur utilisateur = utilisateurService.findByEmail(email);
        return creerReservation(request, utilisateur.getId());
    }
    
    public List<ReservationResponse> getReservationsByUtilisateur(String email) {
        Utilisateur utilisateur = utilisateurService.findByEmail(email);
        return getReservationsUtilisateur(utilisateur.getId());
    }
    
    public ReservationResponse getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        return convertToResponse(reservation);
    }
    
    public ReservationResponse annulerReservation(Long id, String email) {
        Utilisateur utilisateur = utilisateurService.findByEmail(email);
        annulerReservation(id, utilisateur.getId());
        return getReservationById(id);
    }
    
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    private ReservationResponse convertToResponse(Reservation reservation) {
        List<BilletResponse> billetsResponse = new ArrayList<>();
        if (reservation.getBillets() != null) {
            billetsResponse = reservation.getBillets().stream()
                    .map(billet -> BilletResponse.builder()
                            .id(billet.getId())
                            .numeroPlace(billet.getNumeroPlace())
                            .codeQR(billet.getCodeQR())
                            .dateGeneration(billet.getDateGeneration())
                            .utilise(billet.getUtilise())
                            .dateUtilisation(billet.getDateUtilisation())
                            .build())
                    .collect(Collectors.toList());
        }
        
        return ReservationResponse.builder()
                .id(reservation.getId())
                .utilisateurId(reservation.getUtilisateur().getId())
                .utilisateurEmail(reservation.getUtilisateur().getEmail())
                .utilisateurNom(reservation.getUtilisateur().getNomComplet())
                .spectacleId(reservation.getSpectacle().getId())
                .spectacleTitre(reservation.getSpectacle().getTitre())
                .dateReservation(reservation.getDateReservation())
                .quantite(reservation.getQuantite())
                .montantTotal(reservation.getMontantTotal())
                .statut(reservation.getStatut())
                .codeConfirmation(reservation.getCodeConfirmation())
                .billets(billetsResponse)
                .build();
    }
}
