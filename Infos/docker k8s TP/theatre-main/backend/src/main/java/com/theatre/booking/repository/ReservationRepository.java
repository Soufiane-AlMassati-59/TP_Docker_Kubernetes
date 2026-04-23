package com.theatre.booking.repository;

import com.theatre.booking.model.Reservation;
import com.theatre.booking.model.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Reservation
 * Pattern: Repository Pattern
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    List<Reservation> findByUtilisateurIdOrderByDateReservationDesc(Long utilisateurId);
    
    List<Reservation> findBySpectacleId(Long spectacleId);
    
    Optional<Reservation> findByCodeConfirmation(String codeConfirmation);
    
    List<Reservation> findByStatut(StatutReservation statut);
    
    @Query("SELECT r FROM Reservation r WHERE r.utilisateur.id = :utilisateurId AND r.statut = :statut")
    List<Reservation> findByUtilisateurIdAndStatut(Long utilisateurId, StatutReservation statut);
    
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.spectacle.id = :spectacleId AND r.statut = 'CONFIRMEE'")
    Long countReservationsConfirmeesParSpectacle(Long spectacleId);
}
