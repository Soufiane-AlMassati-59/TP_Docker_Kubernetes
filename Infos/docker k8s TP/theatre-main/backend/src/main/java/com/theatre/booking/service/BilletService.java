package com.theatre.booking.service;

import com.theatre.booking.model.Billet;
import com.theatre.booking.model.Reservation;
import com.theatre.booking.repository.BilletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service pour la gestion des billets
 * Pattern: Factory Pattern
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BilletService {
    
    private final BilletRepository billetRepository;
    
    /**
     * Factory Method: Génère les billets pour une réservation
     * Pattern: Factory Pattern
     */
    public List<Billet> genererBillets(Reservation reservation, int quantite) {
        List<Billet> billets = new ArrayList<>();
        
        for (int i = 0; i < quantite; i++) {
            Billet billet = Billet.builder()
                    .reservation(reservation)
                    .numeroPlace(genererNumeroPlace())
                    .codeQR(genererCodeQR(reservation.getCodeConfirmation(), i))
                    .utilise(false)
                    .build();
            
            billets.add(billetRepository.save(billet));
        }
        
        return billets;
    }
    
    public Billet validerBillet(String codeQR) {
        Billet billet = billetRepository.findByCodeQR(codeQR)
                .orElseThrow(() -> new RuntimeException("Billet non trouvé"));
        
        if (!billet.estValide()) {
            throw new RuntimeException("Billet non valide ou déjà utilisé");
        }
        
        try {
            billet.marquerUtilise();
            return billetRepository.save(billet);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la validation: " + e.getMessage());
        }
    }
    
    private String genererNumeroPlace() {
        // Génération simple - pourrait être amélioré avec un système de placement réel
        char rangee = (char) ('A' + (int) (Math.random() * 10));
        int numero = (int) (Math.random() * 50) + 1;
        return rangee + String.format("%02d", numero);
    }
    
    private String genererCodeQR(String codeConfirmation, int index) {
        return "QR-" + codeConfirmation + "-" + index + "-" + UUID.randomUUID().toString().substring(0, 6);
    }
}
