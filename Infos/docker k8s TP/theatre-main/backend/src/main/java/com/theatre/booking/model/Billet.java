package com.theatre.booking.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entité Billet
 * Pattern: Entity (JPA)
 */
@Entity
@Table(name = "billets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Billet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_billet")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reservation", nullable = false)
    private Reservation reservation;
    
    @Column(name = "numero_place", length = 10)
    private String numeroPlace;
    
    @Column(nullable = false, unique = true, name = "code_qr")
    private String codeQR;
    
    @Column(name = "date_generation")
    private LocalDateTime dateGeneration = LocalDateTime.now();
    
    @Column(nullable = false)
    private Boolean utilise = false;
    
    @Column(name = "date_utilisation")
    private LocalDateTime dateUtilisation;
    
    /**
     * Marque le billet comme utilisé
     */
    public void marquerUtilise() throws Exception {
        if (this.utilise) {
            throw new Exception("Billet déjà utilisé");
        }
        this.utilise = true;
        this.dateUtilisation = LocalDateTime.now();
    }
    
    /**
     * Vérifie si le billet est valide
     */
    public boolean estValide() {
        return !this.utilise
            && reservation.getStatut() == StatutReservation.CONFIRMEE
            && !reservation.getSpectacle().getDateSpectacle()
                .isBefore(LocalDate.now());
    }
}
