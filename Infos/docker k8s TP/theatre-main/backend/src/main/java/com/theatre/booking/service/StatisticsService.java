package com.theatre.booking.service;

import com.theatre.booking.dto.response.StatisticsResponse;
import com.theatre.booking.model.Reservation;
import com.theatre.booking.model.Spectacle;
import com.theatre.booking.model.StatutReservation;
import com.theatre.booking.repository.ReservationRepository;
import com.theatre.booking.repository.SpectacleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour les statistiques (Admin)
 * Pattern: Service Layer Pattern
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {
    
    private final ReservationRepository reservationRepository;
    private final SpectacleRepository spectacleRepository;
    
    public StatisticsResponse getStatistiquesGlobales() {
        List<Reservation> reservationsConfirmees = reservationRepository
                .findByStatut(StatutReservation.CONFIRMEE);
        
        Long totalReservations = (long) reservationsConfirmees.size();
        Long totalBilletsVendus = reservationsConfirmees.stream()
                .mapToLong(Reservation::getQuantite)
                .sum();
        
        BigDecimal revenuTotal = reservationsConfirmees.stream()
                .map(Reservation::getMontantTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        List<Spectacle> spectacles = spectacleRepository.findAll();
        Double tauxRemplissageMoyen = spectacles.stream()
                .mapToDouble(Spectacle::getTauxRemplissage)
                .average()
                .orElse(0.0);
        
        List<StatisticsResponse.SpectacleStats> spectaclesPopulaires = 
                getSpectaclesPopulaires();
        
        return StatisticsResponse.builder()
                .totalReservations(totalReservations)
                .totalBilletsVendus(totalBilletsVendus)
                .revenuTotal(revenuTotal)
                .tauxRemplissageMoyen(tauxRemplissageMoyen)
                .spectaclesPopulaires(spectaclesPopulaires)
                .build();
    }
    
    public List<StatisticsResponse.SpectacleStats> getSpectaclesPopulaires() {
        return getSpectaclesPopulaires(10);
    }
    
    public List<StatisticsResponse.SpectacleStats> getSpectaclesPopulaires(int limit) {
        return spectacleRepository.findAll().stream()
                .map(spectacle -> {
                    List<Reservation> reservations = reservationRepository
                            .findBySpectacleId(spectacle.getId())
                            .stream()
                            .filter(r -> r.getStatut() == StatutReservation.CONFIRMEE)
                            .collect(Collectors.toList());
                    
                    Long nombreReservations = (long) reservations.size();
                    Long billetsVendus = reservations.stream()
                            .mapToLong(Reservation::getQuantite)
                            .sum();
                    BigDecimal revenu = reservations.stream()
                            .map(Reservation::getMontantTotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    return StatisticsResponse.SpectacleStats.builder()
                            .spectacleId(spectacle.getId())
                            .titre(spectacle.getTitre())
                            .nombreReservations(nombreReservations)
                            .billetsVendus(billetsVendus)
                            .revenu(revenu)
                            .tauxRemplissage(spectacle.getTauxRemplissage())
                            .build();
                })
                .sorted((s1, s2) -> Long.compare(s2.getBilletsVendus(), s1.getBilletsVendus()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
