package com.example.projet_gestion_absences.controller;

//package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.dto.AbsenceResponseDTO;
import com.example.projet_gestion_absences.model.entity.Absence;
import com.example.projet_gestion_absences.repository.AbsenceRepository;
import com.example.projet_gestion_absences.service.AbsenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/absences")
@PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")
public class AbsenceGlobalController {

    private final AbsenceRepository absenceRepo;
    private final AbsenceService absenceService;

    public AbsenceGlobalController(AbsenceRepository absenceRepo, AbsenceService absenceService) {
        this.absenceRepo = absenceRepo;
        this.absenceService = absenceService;
    }

    /* ===================== DTO helper ===================== */
    private static AbsenceResponseDTO toDto(Absence a) {
        AbsenceResponseDTO dto = new AbsenceResponseDTO();
        dto.setId(a.getId());
        dto.setEtudiantId(a.getEtudiant().getId());
        // entité: isJustifiee() -> DTO: setJustifie(...)
        dto.setJustifie(a.isJustifiee());
        dto.setMotif(a.getMotif());
        return dto;
    }

    /* ===================== 1) toutes les absences ===================== */
    @GetMapping
    public ResponseEntity<List<AbsenceResponseDTO>> getAll() {
        List<AbsenceResponseDTO> out = absenceRepo.findAll()
                .stream()
                .map(AbsenceGlobalController::toDto)
                .toList();
        return ResponseEntity.ok(out);
    }

    /* ===================== 2) absences par étudiant ===================== */
    @GetMapping("/etudiants/{etudiantId}")
    public ResponseEntity<List<AbsenceResponseDTO>> getByEtudiant(@PathVariable Long etudiantId) {
        // Si tu ajoutes plus tard: List<Absence> findByEtudiantId(Long etudiantId)
        // dans AbsenceRepository, remplace le stream filtre par l’appel repo direct.
        List<AbsenceResponseDTO> out = absenceRepo.findAll()
                .stream()
                .filter(a -> a.getEtudiant() != null && etudiantId.equals(a.getEtudiant().getId()))
                .map(AbsenceGlobalController::toDto)
                .toList();
        return ResponseEntity.ok(out);
    }

    /* ===================== 3) absences par séance ===================== */
    // NOTE: tu as déjà /api/seances/{seanceId}/absences dans AbsenceController,
    // on expose ici un alias pratique côté /api/absences/...
    @GetMapping("/seances/{seanceId}")
    public ResponseEntity<List<AbsenceResponseDTO>> getBySeance(@PathVariable Long seanceId) {
        return ResponseEntity.ok(absenceService.listBySeance(seanceId));
    }

    /* ===================== 4) stats par séance ===================== */
    public record CountResponse(long total, long justifiees, long nonJustifiees) {}

    @GetMapping("/seances/{seanceId}/count")
    public ResponseEntity<CountResponse> countBySeance(@PathVariable Long seanceId) {
        long total = absenceService.countBySeance(seanceId);
        long just  = absenceService.countJustifieesBySeance(seanceId);
        long non   = total - just;
        return ResponseEntity.ok(new CountResponse(total, just, non));
    }
}
