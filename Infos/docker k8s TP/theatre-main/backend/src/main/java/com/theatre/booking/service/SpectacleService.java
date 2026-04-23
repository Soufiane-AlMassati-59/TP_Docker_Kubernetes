package com.theatre.booking.service;

import com.theatre.booking.dto.request.SpectacleRequest;
import com.theatre.booking.dto.response.SpectacleResponse;
import com.theatre.booking.model.Spectacle;
import com.theatre.booking.repository.SpectacleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des spectacles
 * Pattern: Service Layer Pattern
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SpectacleService {
    
    private final SpectacleRepository spectacleRepository;
    
    public SpectacleResponse creerSpectacle(SpectacleRequest request) {
        Spectacle spectacle = Spectacle.builder()
                .titre(request.getTitre())
                .description(request.getDescription())
                .dateSpectacle(request.getDateSpectacle())
                .heureDebut(request.getHeureDebut())
                .prixUnitaire(request.getPrixUnitaire())
                .imageUrl(request.getImageUrl())
                .placesTotales(request.getPlacesTotales())
                .placesDisponibles(request.getPlacesTotales())
                .categorie(request.getCategorie())
                .actif(true)
                .build();
        
        Spectacle saved = spectacleRepository.save(spectacle);
        return convertToResponse(saved);
    }
    
    public SpectacleResponse modifierSpectacle(Long id, SpectacleRequest request) {
        Spectacle spectacle = spectacleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spectacle non trouvé"));
        
        spectacle.setTitre(request.getTitre());
        spectacle.setDescription(request.getDescription());
        spectacle.setDateSpectacle(request.getDateSpectacle());
        spectacle.setHeureDebut(request.getHeureDebut());
        spectacle.setPrixUnitaire(request.getPrixUnitaire());
        spectacle.setImageUrl(request.getImageUrl());
        spectacle.setCategorie(request.getCategorie());
        
        // Ne pas modifier les places si des réservations existent
        if (spectacle.getPlacesDisponibles().equals(spectacle.getPlacesTotales())) {
            spectacle.setPlacesTotales(request.getPlacesTotales());
            spectacle.setPlacesDisponibles(request.getPlacesTotales());
        }
        
        Spectacle saved = spectacleRepository.save(spectacle);
        return convertToResponse(saved);
    }
    
    public void supprimerSpectacle(Long id) {
        Spectacle spectacle = spectacleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spectacle non trouvé"));
        
        // Soft delete
        spectacle.setActif(false);
        spectacleRepository.save(spectacle);
    }
    
    public SpectacleResponse getSpectacle(Long id) {
        Spectacle spectacle = spectacleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spectacle non trouvé"));
        return convertToResponse(spectacle);
    }
    
    public List<SpectacleResponse> getAllSpectaclesActifs() {
        return spectacleRepository.findAllSpectaclesActifs()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<SpectacleResponse> getSpectaclesDisponibles() {
        return spectacleRepository.findByDateSpectacleAfterAndActifTrue(LocalDate.now())
                .stream()
                .filter(s -> s.getPlacesDisponibles() > 0)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<SpectacleResponse> rechercherSpectacles(String keyword) {
        return spectacleRepository.searchByTitre(keyword)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Méthodes pour les contrôleurs
    public List<SpectacleResponse> getAllSpectacles() {
        return spectacleRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public SpectacleResponse getSpectacleById(Long id) {
        return getSpectacle(id);
    }
    
    public List<SpectacleResponse> getSpectaclesAVenir() {
        return getSpectaclesDisponibles();
    }
    
    public List<SpectacleResponse> rechercherParTitre(String query) {
        return rechercherSpectacles(query);
    }
    
    public SpectacleResponse createSpectacle(SpectacleRequest request) {
        return creerSpectacle(request);
    }
    
    public SpectacleResponse updateSpectacle(Long id, SpectacleRequest request) {
        return modifierSpectacle(id, request);
    }
    
    public void deleteSpectacle(Long id) {
        supprimerSpectacle(id);
    }
    
    private SpectacleResponse convertToResponse(Spectacle spectacle) {
        return SpectacleResponse.builder()
                .id(spectacle.getId())
                .titre(spectacle.getTitre())
                .description(spectacle.getDescription())
                .dateSpectacle(spectacle.getDateSpectacle())
                .heureDebut(spectacle.getHeureDebut())
                .prixUnitaire(spectacle.getPrixUnitaire())
                .imageUrl(spectacle.getImageUrl())
                .placesTotales(spectacle.getPlacesTotales())
                .placesDisponibles(spectacle.getPlacesDisponibles())
                .categorie(spectacle.getCategorie())
                .actif(spectacle.getActif())
                .tauxRemplissage(spectacle.getTauxRemplissage())
                .build();
    }
}
