package com.theatre.booking.repository;

import com.theatre.booking.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité Utilisateur
 * Pattern: Repository Pattern
 */
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    
    Optional<Utilisateur> findByEmail(String email);
    
    Boolean existsByEmail(String email);
}
