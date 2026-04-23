package com.theatre.booking.repository;

import com.theatre.booking.model.Spectacle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository pour l'entité Spectacle
 * Pattern: Repository Pattern
 */
@Repository
public interface SpectacleRepository extends JpaRepository<Spectacle, Long> {
    
    List<Spectacle> findByDateSpectacleAfterAndActifTrue(LocalDate date);
    
    List<Spectacle> findByCategorie(String categorie);
    
    List<Spectacle> findByPlacesDisponiblesGreaterThanAndActifTrue(Integer places);
    
    @Query("SELECT s FROM Spectacle s WHERE s.actif = true ORDER BY s.dateSpectacle ASC")
    List<Spectacle> findAllSpectaclesActifs();
    
    @Query("SELECT s FROM Spectacle s WHERE LOWER(s.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) AND s.actif = true")
    List<Spectacle> searchByTitre(String keyword);
}
