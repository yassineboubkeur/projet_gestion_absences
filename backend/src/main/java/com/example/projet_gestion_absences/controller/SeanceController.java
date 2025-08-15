package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.dto.SeanceDTO;
import com.example.projet_gestion_absences.model.entity.Seance;
import com.example.projet_gestion_absences.service.SeanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createSeance(@RequestBody SeanceDTO seanceDTO) {
        if (seanceService.hasScheduleConflict(seanceDTO)) {
            return ResponseEntity.badRequest().body("Schedule conflict detected");
        }
        Seance createdSeance = seanceService.createSeance(seanceDTO);
        return ResponseEntity.ok(createdSeance);
    }

    // Read
    @GetMapping
    public List<Seance> getAllSeances() {
        return seanceService.getAllSeances();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seance> getSeanceById(@PathVariable Long id) {
        return seanceService.getSeanceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/date/{date}")
    public List<Seance> getSeancesByDate(@PathVariable LocalDate date) {
        return seanceService.getSeancesByDate(date);
    }

    @GetMapping("/professeur/{professeurId}")
    public List<Seance> getSeancesByProfesseur(@PathVariable Long professeurId) {
        return seanceService.getSeancesByProfesseur(professeurId);
    }

    @GetMapping("/cours/{coursId}")
    public List<Seance> getSeancesByCours(@PathVariable Long coursId) {
        return seanceService.getSeancesByCours(coursId);
    }

    @GetMapping("/salle/{salleId}")
    public List<Seance> getSeancesBySalle(@PathVariable Long salleId) {
        return seanceService.getSeancesBySalle(salleId);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSeance(@PathVariable Long id, @RequestBody SeanceDTO seanceDTO) {
        if (seanceDTO.getDate() != null && seanceDTO.getSalleId() != null &&
                seanceDTO.getHeureDebut() != null && seanceDTO.getHeureFin() != null) {
            if (seanceService.hasScheduleConflict(seanceDTO)) {
                return ResponseEntity.badRequest().body("Schedule conflict detected");
            }
        }
        try {
            Seance updatedSeance = seanceService.updateSeance(id, seanceDTO);
            return ResponseEntity.ok(updatedSeance);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeance(@PathVariable Long id) {
        try {
            seanceService.deleteSeance(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Check schedule conflict
    @PostMapping("/check-conflict")
    public ResponseEntity<Boolean> checkScheduleConflict(@RequestBody SeanceDTO seanceDTO) {
        boolean hasConflict = seanceService.hasScheduleConflict(seanceDTO);
        return ResponseEntity.ok(hasConflict);
    }
}