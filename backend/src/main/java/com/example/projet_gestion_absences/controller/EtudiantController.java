package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.entity.Etudiant;
import com.example.projet_gestion_absences.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    @PostMapping
    public ResponseEntity<Etudiant> addEtudiant(@RequestBody Etudiant etudiant) {
        Etudiant savedEtudiant = etudiantService.save(etudiant);
        return ResponseEntity.ok(savedEtudiant);
    }

    @GetMapping
    public List<Etudiant> getAllEtudiants() {
        return etudiantService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Long id) {
        return etudiantService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public Etudiant registerEtudiant(@RequestBody Etudiant etudiant) {
        // Ici tu peux ajouter la logique de hash du password, validation, etc.
        return etudiantService.save(etudiant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable Long id, @RequestBody Etudiant etudiantDetails) {
        return etudiantService.findById(id)
                .map(etudiant -> {
                    etudiant.setNom(etudiantDetails.getNom());
                    etudiant.setPrenom(etudiantDetails.getPrenom());
                    etudiant.setEmail(etudiantDetails.getEmail());
                    etudiant.setLogin(etudiantDetails.getLogin());
                    etudiant.setPassword(etudiantDetails.getPassword()); // Hasher avant en vrai
                    etudiant.setActive(etudiantDetails.isActive());
                    etudiant.setMatricule(etudiantDetails.getMatricule());
                    etudiant.setAddress(etudiantDetails.getAddress());
                    Etudiant updated = etudiantService.save(etudiant);
                    return ResponseEntity.ok(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        if (etudiantService.findById(id).isPresent()) {
            etudiantService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
