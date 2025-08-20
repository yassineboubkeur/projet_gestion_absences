package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.dto.SeanceDTO;
import com.example.projet_gestion_absences.model.dto.SeanceResponseDTO;
import com.example.projet_gestion_absences.service.SeanceService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/seances")
public class SeanceController {

    private final SeanceService seanceService;

    public SeanceController(SeanceService seanceService) {
        this.seanceService = seanceService;
    }

    // Create
    @PostMapping
    public ResponseEntity<SeanceResponseDTO> createSeance(@Valid @RequestBody SeanceDTO seanceDTO) {
        if (seanceService.hasScheduleConflict(seanceDTO)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
        SeanceResponseDTO created = seanceService.createSeance(seanceDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/seances/" + created.id()));
        return new ResponseEntity<>(created, headers, HttpStatus.CREATED); // 201 + Location
    }

    // Read
    @GetMapping
    public ResponseEntity<List<SeanceResponseDTO>> getAllSeances() {
        return ResponseEntity.ok(seanceService.getAllSeances());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeanceResponseDTO> getSeanceById(@PathVariable Long id) {
        return ResponseEntity.ok(seanceService.getSeanceById(id));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<SeanceResponseDTO>> getSeancesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(seanceService.getSeancesByDate(date));
    }

    @GetMapping("/professeur/{professeurId}")
    public ResponseEntity<List<SeanceResponseDTO>> getSeancesByProfesseur(@PathVariable Long professeurId) {
        return ResponseEntity.ok(seanceService.getSeancesByProfesseur(professeurId));
    }

    @GetMapping("/cours/{coursId}")
    public ResponseEntity<List<SeanceResponseDTO>> getSeancesByCours(@PathVariable Long coursId) {
        return ResponseEntity.ok(seanceService.getSeancesByCours(coursId));
    }

    @GetMapping("/salle/{salleId}")
    public ResponseEntity<List<SeanceResponseDTO>> getSeancesBySalle(@PathVariable Long salleId) {
        return ResponseEntity.ok(seanceService.getSeancesBySalle(salleId));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<SeanceResponseDTO> updateSeance(@PathVariable Long id,
                                                          @Valid @RequestBody SeanceDTO seanceDTO) {
        // On ne bloque que si on a de quoi vérifier un vrai chevauchement
        if (seanceDTO.getDate() != null &&
                seanceDTO.getSalleId() != null &&
                seanceDTO.getHeureDebut() != null &&
                seanceDTO.getHeureFin() != null &&
                seanceService.hasScheduleConflict(seanceDTO)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
        return ResponseEntity.ok(seanceService.updateSeance(id, seanceDTO));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeance(@PathVariable Long id) {
        seanceService.deleteSeance(id);
        return ResponseEntity.noContent().build(); // 204
    }

    // Check schedule conflict
    @PostMapping("/check-conflict")
    public ResponseEntity<Boolean> checkScheduleConflict(@Valid @RequestBody SeanceDTO seanceDTO) {
        return ResponseEntity.ok(seanceService.hasScheduleConflict(seanceDTO));
    }
}
