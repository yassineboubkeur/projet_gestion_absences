package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.dto.AbsenceDTO;
import com.example.projet_gestion_absences.model.dto.AbsenceResponseDTO;
import com.example.projet_gestion_absences.service.AbsenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seances/{seanceId}/absences")
@PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")

public class AbsenceController {

    private final AbsenceService absenceService;

    public AbsenceController(AbsenceService absenceService) {
        this.absenceService = absenceService;
    }
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")
    @GetMapping
    public ResponseEntity<List<AbsenceResponseDTO>> list(@PathVariable Long seanceId) {
        return ResponseEntity.ok(absenceService.listBySeance(seanceId));
    }

    // petite DTO de réponse pour le count
    public record CountResponse(long total, long justifiees, long nonJustifiees) {}
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")

    @GetMapping("/count")

    public ResponseEntity<CountResponse> count(@PathVariable Long seanceId) {
        long total = absenceService.countBySeance(seanceId);
        long just   = absenceService.countJustifieesBySeance(seanceId);
        long non    = total - just;
        return ResponseEntity.ok(new CountResponse(total, just, non));
    }

    // wrapper pour le bulk: { "absences": [ {etudiantId, justifie, motif}, ... ] }
    public static class BulkRequest {
        private List<AbsenceDTO> absences;
        public List<AbsenceDTO> getAbsences() { return absences; }
        public void setAbsences(List<AbsenceDTO> absences) { this.absences = absences; }
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")

    public ResponseEntity<Void> bulk(@PathVariable Long seanceId, @RequestBody BulkRequest body) {
        absenceService.saveBulk(seanceId, body.getAbsences() != null ? body.getAbsences() : List.of());
        return ResponseEntity.noContent().build();
    }
}



//package com.example.projet_gestion_absences.controller;
//
//import com.example.projet_gestion_absences.model.dto.AbsenceDTO;
//import com.example.projet_gestion_absences.model.dto.AbsenceResponseDTO;
//import com.example.projet_gestion_absences.service.AbsenceService;
//import jakarta.validation.Valid;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/seances/{seanceId}/absences")
//public class AbsenceController {
//
//    private final AbsenceService absenceService;
//
//    public AbsenceController(AbsenceService absenceService) {
//        this.absenceService = absenceService;
//    }
//
//    // GET: liste des absences (DTO déjà mappés par le service)
//    @GetMapping
//    public ResponseEntity<List<AbsenceResponseDTO>> list(@PathVariable Long seanceId) {
//        return ResponseEntity.ok(absenceService.listBySeance(seanceId));
//    }
//
//    // GET: stats (total / justifiees / nonJustifiees)
//    @GetMapping("/count")
//    public ResponseEntity<Map<String, Long>> count(@PathVariable Long seanceId) {
//        return ResponseEntity.ok(absenceService.statsBySeance(seanceId));
//    }
//
//    // Requête pour le bulk
//    public static class BulkRequest {
//        private List<@Valid AbsenceDTO> absences;
//
//        public List<AbsenceDTO> getAbsences() { return absences; }
//        public void setAbsences(List<AbsenceDTO> absences) { this.absences = absences; }
//    }
//
//    // POST: création/màj en masse (remplace l’état)
//    @PostMapping("/bulk")
//    public ResponseEntity<Void> bulk(@PathVariable Long seanceId,
//                                     @Valid @RequestBody(required = false) BulkRequest req) {
//        List<AbsenceDTO> items = (req != null) ? req.getAbsences() : List.of();
//        absenceService.saveBulk(seanceId, items);
//        return ResponseEntity.noContent().build();
//    }
//}
