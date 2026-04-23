package com.theatre.booking.controller;

import com.theatre.booking.dto.response.SpectacleResponse;
import com.theatre.booking.dto.response.StatisticsResponse;
import com.theatre.booking.service.SpectacleService;
import com.theatre.booking.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour les statistiques
 * Pattern: MVC Controller
 */
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StatisticsController {
    
    private final StatisticsService statisticsService;
    private final SpectacleService spectacleService;
    
    /**
     * Récupérer les statistiques globales (Admin uniquement)
     */
    @GetMapping("/global")
    public ResponseEntity<StatisticsResponse> getGlobalStatistics() {
        StatisticsResponse statistics = statisticsService.getStatistiquesGlobales();
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * Récupérer les spectacles les plus populaires (simplifiébasé sur les places disponibles)
     */
    @GetMapping("/popular")
    public ResponseEntity<List<SpectacleResponse>> getPopularSpectacles(
            @RequestParam(defaultValue = "5") int limit) {
        // Pour l'instant, on retourne les spectacles à venir
        List<SpectacleResponse> spectacles = spectacleService.getSpectaclesAVenir();
        return ResponseEntity.ok(spectacles.stream().limit(limit).toList());
    }
}
