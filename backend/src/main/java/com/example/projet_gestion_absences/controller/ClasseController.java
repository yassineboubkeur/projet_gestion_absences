package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.dto.ClasseDTO;
import com.example.projet_gestion_absences.model.dto.ClasseResponseDTO;
import com.example.projet_gestion_absences.model.dto.EtudiantResponseDTO;
import com.example.projet_gestion_absences.service.ClasseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClasseController {

    private final ClasseService classeService;

    public ClasseController(ClasseService classeService) {
        this.classeService = classeService;
    }

    // CREATE
    @PostMapping
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
    public ResponseEntity<ClasseResponseDTO> updateClasse(
            @PathVariable Long id,
            @RequestBody ClasseDTO classeDTO) {
        return classeService.updateClasse(id, classeDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClasse(@PathVariable Long id) {
        classeService.deleteClasse(id);
        return ResponseEntity.noContent().build();
    }

    // SPECIAL ENDPOINTS
    @GetMapping("/{id}/etudiants")
    public ResponseEntity<List<EtudiantResponseDTO>> getEtudiantsByClasse(@PathVariable Long id) {
        List<EtudiantResponseDTO> etudiants = classeService.getEtudiantsByClasse(id);
        return ResponseEntity.ok(etudiants);
    }

    @GetMapping("/niveau/{niveau}")
    public List<ClasseResponseDTO> getClassesByNiveau(@PathVariable String niveau) {
        return classeService.getClassesByNiveau(niveau);
    }
}