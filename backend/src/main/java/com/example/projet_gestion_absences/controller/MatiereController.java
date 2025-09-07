package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.dto.MatiereDTO;
import com.example.projet_gestion_absences.model.entity.Matiere;
import com.example.projet_gestion_absences.service.MatiereService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/matieres")
@PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")

public class MatiereController {

    private final MatiereService matiereService;

    public MatiereController(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    // Create
    @PostMapping
    public Matiere createMatiere(@Valid @RequestBody MatiereDTO matiereDTO) {
        return matiereService.createMatiere(matiereDTO);
    }

    // Read
    @GetMapping
    public List<Matiere> getAllMatieres() {
        return matiereService.getAllMatieres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Matiere> getMatiereById(@PathVariable Long id) {
        return matiereService.getMatiereById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/domaine/{domaine}")
    public List<Matiere> getMatieresByDomaine(@PathVariable String domaine) {
        return matiereService.getMatieresByDomaine(domaine);
    }

    @GetMapping("/search")
    public List<Matiere> searchByIntitule(@RequestParam String intitule) {
        return matiereService.searchByIntitule(intitule);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Matiere> getMatiereByCode(@PathVariable String code) {
        Matiere matiere = matiereService.getMatiereByCode(code);
        if (matiere != null) {
            return ResponseEntity.ok(matiere);
        }
        return ResponseEntity.notFound().build();
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Matiere> updateMatiere(
            @PathVariable Long id,
            @Valid @RequestBody MatiereDTO matiereDTO) {
        try {

            Matiere updatedMatiere = matiereService.updateMatiere(id, matiereDTO);
            return ResponseEntity.ok(updatedMatiere);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatiere(@PathVariable Long id) {
        try {
            matiereService.deleteMatiere(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}