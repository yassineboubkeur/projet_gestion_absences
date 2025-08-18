package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.dto.EtudiantDTO;
import com.example.projet_gestion_absences.model.dto.EtudiantResponseDTO;
import com.example.projet_gestion_absences.model.entity.Etudiant;
import com.example.projet_gestion_absences.service.EtudiantService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
@SecurityRequirement(name = "bearerAuth")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    // Create student
    @PostMapping
    public ResponseEntity<EtudiantResponseDTO> createEtudiant(@RequestBody EtudiantDTO etudiantDTO) {
        EtudiantResponseDTO savedEtudiant = etudiantService.createEtudiant(etudiantDTO);
        return ResponseEntity.ok(savedEtudiant);
    }

    // Get all students
    @GetMapping
    public List<EtudiantResponseDTO> getAllEtudiants() {
        return etudiantService.getAllEtudiants();
    }

    // Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<EtudiantResponseDTO> getEtudiantById(@PathVariable Long id) {
        return etudiantService.getEtudiantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update student
    @PutMapping("/{id}")
    public ResponseEntity<EtudiantResponseDTO> updateEtudiant(
            @PathVariable Long id,
            @RequestBody EtudiantDTO etudiantDTO) {
        return etudiantService.updateEtudiant(id, etudiantDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteEtudiant(id);
        return ResponseEntity.noContent().build();
    }

    // Class-related endpoints

    // Get students by class
    @GetMapping("/by-classe/{classeId}")
    public List<EtudiantResponseDTO> getEtudiantsByClasse(@PathVariable Long classeId) {
        return etudiantService.getEtudiantsByClasse(classeId);
    }

    // Change student's class
    @PatchMapping("/{etudiantId}/change-classe/{newClasseId}")
    public ResponseEntity<EtudiantResponseDTO> changeClasse(
            @PathVariable Long etudiantId,
            @PathVariable Long newClasseId) {
        return etudiantService.changeClasse(etudiantId, newClasseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Register student (kept for backward compatibility)
    @PostMapping("/register")
    public ResponseEntity<EtudiantResponseDTO> registerEtudiant(@RequestBody EtudiantDTO etudiantDTO) {
        return createEtudiant(etudiantDTO);
    }
}



//package com.example.projet_gestion_absences.controller;
//
//import com.example.projet_gestion_absences.model.entity.Etudiant;
//import com.example.projet_gestion_absences.service.EtudiantService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/etudiants")
//public class EtudiantController {
//
//    @Autowired
//    private EtudiantService etudiantService;
//
//    @PostMapping
//    public ResponseEntity<Etudiant> addEtudiant(@RequestBody Etudiant etudiant) {
//        Etudiant savedEtudiant = etudiantService.save(etudiant);
//        return ResponseEntity.ok(savedEtudiant);
//    }
//
//    @GetMapping
//    public List<Etudiant> getAllEtudiants() {
//        return etudiantService.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Long id) {
//        return etudiantService.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping("/register")
//    public Etudiant registerEtudiant(@RequestBody Etudiant etudiant) {
//        // Ici tu peux ajouter la logique de hash du password, validation, etc.
//        return etudiantService.save(etudiant);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable Long id, @RequestBody Etudiant etudiantDetails) {
//        return etudiantService.findById(id)
//                .map(etudiant -> {
//                    etudiant.setNom(etudiantDetails.getNom());
//                    etudiant.setPrenom(etudiantDetails.getPrenom());
//                    etudiant.setEmail(etudiantDetails.getEmail());
//                    etudiant.setLogin(etudiantDetails.getLogin());
//                    etudiant.setPassword(etudiantDetails.getPassword()); // Hasher avant en vrai
//                    etudiant.setActive(etudiantDetails.isActive());
//                    etudiant.setMatricule(etudiantDetails.getMatricule());
//                    etudiant.setAddress(etudiantDetails.getAddress());
//                    Etudiant updated = etudiantService.save(etudiant);
//                    return ResponseEntity.ok(updated);
//                }).orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
//        if (etudiantService.findById(id).isPresent()) {
//            etudiantService.deleteById(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//}
