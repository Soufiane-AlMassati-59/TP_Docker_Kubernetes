package com.theatre.booking.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_spectacle", nullable = false)
    private Spectacle spectacle;
    
    @Column(name = "date_reservation")
    private LocalDateTime dateReservation = LocalDateTime.now();
    
    @Column(nullable = false)
    private Integer quantite;
    
    @Column(nullable = false, name = "montant_total", precision = 10, scale = 2)
    private BigDecimal montantTotal;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReservation statut = StatutReservation.CONFIRMEE;
    
    @Column(nullable = false, unique = true, name = "code_confirmation", length = 50)
    private String codeConfirmation;
    
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Billet> billets;
    
    /**
     * Calcule le montant total de la réservation
     */
    public void calculerMontant() {
        if (spectacle != null) {
            this.montantTotal = spectacle.getPrixUnitaire()
                .multiply(BigDecimal.valueOf(quantite));
        }
    }
    
    /**
     * Confirme la réservation
     */
    public void confirmer() {
        this.statut = StatutReservation.CONFIRMEE;
    }
    
    /**
     * Annule la réservation
     */
    public void annuler() throws Exception {
        if (this.statut == StatutReservation.ANNULEE) {
            throw new Exception("Réservation déjà annulée");
        }
        this.statut = StatutReservation.ANNULEE;
        if (spectacle != null) {
            spectacle.libererPlaces(this.quantite);
        }
    }
    
    /**
     * Vérifie si la réservation peut être annulée
     */
    public boolean peutEtreAnnulee() {
        return this.statut == StatutReservation.CONFIRMEE 
            && spectacle.getDateSpectacle().isAfter(LocalDate.now());
    }
}
