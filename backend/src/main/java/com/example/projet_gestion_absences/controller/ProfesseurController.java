package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.dto.ProfesseurDTO;
import com.example.projet_gestion_absences.model.dto.ProfesseurResponseDTO;
import com.example.projet_gestion_absences.model.dto.SpecialiteWithProfesseursDTO;
import com.example.projet_gestion_absences.service.ProfesseurService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/professeurs")
public class ProfesseurController {

    private final ProfesseurService professeurService;

    public ProfesseurController(ProfesseurService professeurService) {
        this.professeurService = professeurService;
    }


    // CRUD Endpoints
    @PostMapping
    public ResponseEntity<ProfesseurResponseDTO> createProfesseur(@RequestBody ProfesseurDTO professeurDTO) {
        ProfesseurResponseDTO createdProfesseur = professeurService.createProfesseur(professeurDTO);
        return ResponseEntity.ok(createdProfesseur);
    }

    @GetMapping
    public List<ProfesseurResponseDTO> getAllProfesseurs() {
        return professeurService.getAllProfesseurs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesseurResponseDTO> getProfesseurById(@PathVariable Long id) {
        return professeurService.getProfesseurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesseurResponseDTO> updateProfesseur(
            @PathVariable Long id,
            @RequestBody ProfesseurDTO professeurDTO) {
        return professeurService.updateProfesseur(id, professeurDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfesseur(@PathVariable Long id) {
        professeurService.deleteProfesseur(id);
        return ResponseEntity.noContent().build();
    }

    // Search Endpoints
    @GetMapping("/search")
    public List<ProfesseurResponseDTO> searchProfesseurs(@RequestParam String term) {
        return professeurService.searchProfesseurs(term);
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<ProfesseurResponseDTO> getProfesseurByMatricule(@PathVariable String matricule) {
        return professeurService.getProfesseurByMatricule(matricule)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Status Management
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ProfesseurResponseDTO> toggleProfesseurStatus(@PathVariable Long id) {
        return professeurService.toggleProfesseurStatus(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Specialty Endpoints
    @GetMapping("/specialites")
    public ResponseEntity<List<String>> getAllSpecialites() {
        List<String> specialites = professeurService.getAllSpecialites();
        return ResponseEntity.ok(specialites);
    }

    @GetMapping("/specialitesbyprof")
    public ResponseEntity<List<SpecialiteWithProfesseursDTO>> getProfesseursGroupedBySpecialite() {
        List<SpecialiteWithProfesseursDTO> result = professeurService.getProfesseursGroupedBySpecialite();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/specialites/{specialite}")
    public ResponseEntity<List<ProfesseurResponseDTO>> getProfesseursBySpecialite(
            @PathVariable String specialite) {
        List<ProfesseurResponseDTO> professeurs = professeurService.getProfesseursBySpecialite(specialite);
        return ResponseEntity.ok(professeurs);
    }

    @GetMapping("/available")
    @SecurityRequirement(name = "bearerAuth")
// @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSEUR','ROLE_ETUDIANT')") // selon besoin
    public List<ProfesseurResponseDTO> getAvailableProfesseurs(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("start") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime start,
            @RequestParam("end")   @DateTimeFormat(pattern = "HH:mm:ss") LocalTime end
    ) {
        return professeurService.getAvailable(date, start, end);
    }
}