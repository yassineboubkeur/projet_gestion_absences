package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.dto.AbsenceResponseDTO;
import com.example.projet_gestion_absences.model.entity.Absence;
import com.example.projet_gestion_absences.model.entity.Etudiant;
import com.example.projet_gestion_absences.repository.AbsenceRepository;
import com.example.projet_gestion_absences.repository.EtudiantRepository;
import com.example.projet_gestion_absences.service.AbsenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/absences")
public class AbsenceGlobalController {

    private final AbsenceRepository absenceRepo;
    private final AbsenceService absenceService;
    private final EtudiantRepository etudiantRepo;

    public AbsenceGlobalController(
            AbsenceRepository absenceRepo,
            AbsenceService absenceService,
            EtudiantRepository etudiantRepo
    ) {
        this.absenceRepo = absenceRepo;
        this.absenceService = absenceService;
        this.etudiantRepo = etudiantRepo;
    }

    /* ===================== DTO helper ===================== */
    private static AbsenceResponseDTO toDto(Absence a) {
        AbsenceResponseDTO dto = new AbsenceResponseDTO();
        dto.setId(a.getId());
        dto.setEtudiantId(a.getEtudiant() != null ? a.getEtudiant().getId() : null);
        dto.setSeanceId(a.getSeance() != null ? a.getSeance().getId() : null);
        dto.setJustifie(a.isJustifiee());
        dto.setMotif(a.getMotif());

        // LocalDate directement (adapter si ton champ est LocalDateTime côté entité)
        LocalDate d = a.getDateDeclaration();
        dto.setDateDeclaration(d);

        String coursIntitule = (a.getSeance() != null && a.getSeance().getCours() != null)
                ? a.getSeance().getCours().getIntitule()
                : null;
        dto.setCoursIntitule(coursIntitule);

        return dto;
    }

    /* ===================== 1) toutes les absences (Admin/Prof) ===================== */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")
    public ResponseEntity<List<AbsenceResponseDTO>> getAll() {
        List<AbsenceResponseDTO> out = absenceRepo.findAll()
                .stream()
                .map(AbsenceGlobalController::toDto)
                .toList();
        return ResponseEntity.ok(out);
    }

    /* ===================== 2) absences par étudiant (Admin/Prof) ===================== */
    @GetMapping("/etudiants/{etudiantId}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")
    public ResponseEntity<List<AbsenceResponseDTO>> getByEtudiant(@PathVariable Long etudiantId) {
        List<AbsenceResponseDTO> out = absenceRepo.findAll()
                .stream()
                .filter(a -> a.getEtudiant() != null && etudiantId.equals(a.getEtudiant().getId()))
                .map(AbsenceGlobalController::toDto)
                .toList();
        return ResponseEntity.ok(out);
    }

    /* ===================== 3) absences par séance (Admin/Prof) ===================== */
    @GetMapping("/seances/{seanceId}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")
    public ResponseEntity<List<AbsenceResponseDTO>> getBySeance(@PathVariable Long seanceId) {
        return ResponseEntity.ok(absenceService.listBySeance(seanceId));
    }

    /* ===================== 4) stats par séance (Admin/Prof) ===================== */
    public record CountResponse(long total, long justifiees, long nonJustifiees) {}

    @GetMapping("/seances/{seanceId}/count")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")
    public ResponseEntity<CountResponse> countBySeance(@PathVariable Long seanceId) {
        long total = absenceService.countBySeance(seanceId);
        long just  = absenceService.countJustifieesBySeance(seanceId);
        long non   = total - just;
        return ResponseEntity.ok(new CountResponse(total, just, non));
    }

    /* ===================== 5) mes absences (Étudiant) ===================== */
    @GetMapping("/me")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<List<AbsenceResponseDTO>> myAbsences(Authentication authentication) {
        // login (= username) du token
        String login = authentication.getName();

        Etudiant etu = etudiantRepo.findByLogin(login)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Étudiant introuvable pour le login: " + login));

        List<AbsenceResponseDTO> out = absenceRepo.findAll()
                .stream()
                .filter(a -> a.getEtudiant() != null && etu.getId().equals(a.getEtudiant().getId()))
                .map(AbsenceGlobalController::toDto)
                .toList();

        return ResponseEntity.ok(out);
    }
}
