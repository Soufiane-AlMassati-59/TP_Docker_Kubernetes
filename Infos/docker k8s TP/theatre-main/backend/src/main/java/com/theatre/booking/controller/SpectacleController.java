package com.theatre.booking.controller;

import com.theatre.booking.dto.request.SpectacleRequest;
import com.theatre.booking.dto.response.SpectacleResponse;
import com.theatre.booking.service.SpectacleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur REST pour les spectacles
 * Pattern: MVC Controller
 */
@RestController
@RequestMapping("/spectacles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SpectacleController {
    
    private final SpectacleService spectacleService;
    
    /**
     * Récupérer tous les spectacles
     */
    @GetMapping
    public ResponseEntity<List<SpectacleResponse>> getAllSpectacles() {
        List<SpectacleResponse> spectacles = spectacleService.getAllSpectacles();
        return ResponseEntity.ok(spectacles);
    }
    
    /**
     * Récupérer un spectacle par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpectacleResponse> getSpectacleById(@PathVariable Long id) {
        SpectacleResponse spectacle = spectacleService.getSpectacleById(id);
        return ResponseEntity.ok(spectacle);
    }
    
    /**
     * Récupérer les spectacles à venir
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<SpectacleResponse>> getUpcomingSpectacles() {
        List<SpectacleResponse> spectacles = spectacleService.getSpectaclesAVenir();
        return ResponseEntity.ok(spectacles);
    }
    
    /**
     * Rechercher des spectacles par titre
     */
    @GetMapping("/search")
    public ResponseEntity<List<SpectacleResponse>> searchSpectacles(@RequestParam String query) {
        List<SpectacleResponse> spectacles = spectacleService.rechercherParTitre(query);
        return ResponseEntity.ok(spectacles);
    }
    
    /**
     * Créer un nouveau spectacle (Admin uniquement)
     */
    @PostMapping
    public ResponseEntity<SpectacleResponse> createSpectacle(@Valid @RequestBody SpectacleRequest request) {
        SpectacleResponse spectacle = spectacleService.createSpectacle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(spectacle);
    }
    
    /**
     * Mettre à jour un spectacle (Admin uniquement)
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpectacleResponse> updateSpectacle(
            @PathVariable Long id,
            @Valid @RequestBody SpectacleRequest request) {
        SpectacleResponse spectacle = spectacleService.updateSpectacle(id, request);
        return ResponseEntity.ok(spectacle);
    }
    
    /**
     * Supprimer un spectacle (Admin uniquement)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpectacle(@PathVariable Long id) {
        spectacleService.deleteSpectacle(id);
        return ResponseEntity.noContent().build();
    }
}
