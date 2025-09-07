package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.dto.CoursDTO;
import com.example.projet_gestion_absences.model.entity.Cours;
import com.example.projet_gestion_absences.service.CoursService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cours")
@PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")

public class CoursController {

    private final CoursService coursService;

    public CoursController(CoursService coursService) {
        this.coursService = coursService;
    }

    /* =========================
       Create
       ========================= */

    @PostMapping
    public Cours createCours(@RequestBody CoursDTO coursDTO) {
        return coursService.createCours(coursDTO);
    }

    /* =========================
       Helpers d'affectation
       ========================= */

    @PatchMapping("/{coursId}/set-classe/{classeId}")
    public ResponseEntity<Cours> setClasse(@PathVariable Long coursId, @PathVariable Long classeId) {
        return ResponseEntity.ok(coursService.setClasseForCours(coursId, classeId));
    }

    @PatchMapping("/{coursId}/set-professeur/{professeurId}")
    public ResponseEntity<Cours> setProf(@PathVariable Long coursId, @PathVariable Long professeurId) {
        return ResponseEntity.ok(coursService.setProfesseurForCours(coursId, professeurId));
    }

    /* =========================
       Read
       ========================= */

    @GetMapping
    public List<Cours> getAllCours() {
        return coursService.getAllCours();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cours> getCoursById(@PathVariable Long id) {
        return coursService.getCoursById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/matiere/{matiereId}")
    public List<Cours> getCoursByMatiere(@PathVariable Long matiereId) {
        return coursService.getCoursByMatiere(matiereId);
    }

    @GetMapping("/by-classe/{classeId}")
    public List<Cours> getByClasse(@PathVariable Long classeId) {
        return coursService.getCoursByClasse(classeId);
    }

    @GetMapping("/search")
    public List<Cours> searchByIntitule(@RequestParam String intitule) {
        return coursService.searchByIntitule(intitule);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Cours> getCoursByCode(@PathVariable String code) {
        Cours cours = coursService.getCoursByCode(code);
        if (cours != null) {
            return ResponseEntity.ok(cours);
        }
        return ResponseEntity.notFound().build();
    }

    /* =========================
       Update
       ========================= */

    @PutMapping("/{id}")
    public ResponseEntity<Cours> updateCours(@PathVariable Long id, @RequestBody CoursDTO coursDTO) {
        try {
            Cours updatedCours = coursService.updateCours(id, coursDTO);
            return ResponseEntity.ok(updatedCours);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /* =========================
       Delete
       ========================= */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCours(@PathVariable Long id) {
        try {
            coursService.deleteCours(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
