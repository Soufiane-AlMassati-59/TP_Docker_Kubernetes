package com.theatre.booking.repository;

import com.theatre.booking.model.Billet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Billet
 * Pattern: Repository Pattern
 */
@Repository
public interface BilletRepository extends JpaRepository<Billet, Long> {
    
    List<Billet> findByReservationId(Long reservationId);
    
    Optional<Billet> findByCodeQR(String codeQR);
    
    List<Billet> findByUtiliseFalse();
}
