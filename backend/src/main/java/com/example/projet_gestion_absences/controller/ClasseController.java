package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.dto.ClasseDTO;
import com.example.projet_gestion_absences.model.dto.ClasseResponseDTO;
import com.example.projet_gestion_absences.model.dto.EtudiantResponseDTO;
import com.example.projet_gestion_absences.service.ClasseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
//@PreAuthorize("hasRole('ROLE_ADMIN')")

public class ClasseController {

    private final ClasseService classeService;

    public ClasseController(ClasseService classeService) {
        this.classeService = classeService;
    }

    // CREATE
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")

    public ResponseEntity<ClasseResponseDTO> createClasse(@RequestBody ClasseDTO classeDTO) {
        ClasseResponseDTO createdClasse = classeService.createClasse(classeDTO);
        return ResponseEntity.ok(createdClasse);
    }

    // READ ALL
    @GetMapping
    public List<ClasseResponseDTO> getAllClasses() {
        return classeService.getAllClasses();
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<ClasseResponseDTO> getClasseById(@PathVariable Long id) {
        return classeService.getClasseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")

    public ResponseEntity<ClasseResponseDTO> updateClasse(
            @PathVariable Long id,
            @RequestBody ClasseDTO classeDTO) {
        return classeService.updateClasse(id, classeDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")

    public ResponseEntity<Void> deleteClasse(@PathVariable Long id) {
        classeService.deleteClasse(id);
        return ResponseEntity.noContent().build();
    }

    // SPECIAL ENDPOINTS
    @GetMapping("/{id}/etudiants")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")

    public ResponseEntity<List<EtudiantResponseDTO>> getEtudiantsByClasse(@PathVariable Long id) {
        List<EtudiantResponseDTO> etudiants = classeService.getEtudiantsByClasse(id);
        return ResponseEntity.ok(etudiants);
    }

    @GetMapping("/niveau/{niveau}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")

    public List<ClasseResponseDTO> getClassesByNiveau(@PathVariable String niveau) {
        return classeService.getClassesByNiveau(niveau);
    }
}