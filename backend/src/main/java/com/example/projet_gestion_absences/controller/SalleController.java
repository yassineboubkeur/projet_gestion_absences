package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.entity.Salle;
import com.example.projet_gestion_absences.service.SalleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salles")
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
        return salleService.getSalleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
}