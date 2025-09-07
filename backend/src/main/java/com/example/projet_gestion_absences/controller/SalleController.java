package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.entity.Salle;
import com.example.projet_gestion_absences.service.SalleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/salles")
@PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")

public class SalleController {

    private final SalleService salleService;

    public SalleController(SalleService salleService) {
        this.salleService = salleService;
    }

    // Create
    @PostMapping
    public Salle createSalle(@RequestBody Salle salle) {
        return salleService.createSalle(salle);
    }

    // Read
    @GetMapping
    public List<Salle> getAllSalles() {
        return salleService.getAllSalles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Salle> getSalleById(@PathVariable Long id) {
        try {
            // le service renvoie Salle (pas Optional) et lève une RuntimeException si non trouvé
            Salle salle = salleService.getSalleById(id);
            return ResponseEntity.ok(salle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Salle> getSalleByCode(@PathVariable String code) {
        Salle salle = salleService.getSalleByCode(code);
        if (salle != null) {
            return ResponseEntity.ok(salle);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Salle> updateSalle(@PathVariable Long id, @RequestBody Salle salleDetails) {
        try {
            Salle updatedSalle = salleService.updateSalle(id, salleDetails);
            return ResponseEntity.ok(updatedSalle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalle(@PathVariable Long id) {
        try {
            salleService.deleteSalle(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Disponibilités
    @GetMapping("/available")
    @SecurityRequirement(name = "bearerAuth")
    public List<Salle> getAvailableSalles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("start") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime start,
            @RequestParam("end")   @DateTimeFormat(pattern = "HH:mm:ss") LocalTime end
    ) {
        return salleService.getAvailable(date, start, end); // renvoie List<Salle>
    }
}
