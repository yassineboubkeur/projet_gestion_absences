package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.dto.EmploiDuTempsResponseDTO;
import com.example.projet_gestion_absences.model.dto.SeanceDTO;
import com.example.projet_gestion_absences.model.entity.*;
import com.example.projet_gestion_absences.repository.EmploiDuTempsRepository;
import com.example.projet_gestion_absences.service.EmploiDuTempsService;
import com.example.projet_gestion_absences.service.SeanceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emploi-du-temps")
public class EmploiDuTempsController {

    private final EmploiDuTempsService emploiDuTempsService;
    private final EmploiDuTempsRepository emploiDuTempsRepository;
    private final SeanceService seanceService;

    public EmploiDuTempsController(EmploiDuTempsService emploiDuTempsService,
                                   EmploiDuTempsRepository emploiDuTempsRepository,
                                   SeanceService seanceService) {
        this.emploiDuTempsService = emploiDuTempsService;
        this.emploiDuTempsRepository = emploiDuTempsRepository;
        this.seanceService = seanceService;
    }

    // CREATE - Créer un nouvel emploi du temps manuellement
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EmploiDuTempsResponseDTO> createEmploiDuTemps(
            @RequestParam String intitule,
            @RequestParam Long classeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {

        EmploiDuTemps edt = emploiDuTempsService.createEmploiDuTemps(intitule, classeId, dateDebut, dateFin);
        return ResponseEntity.created(URI.create("/api/emploi-du-temps/" + edt.getId()))
                .body(mapEdtToDto(edt));
    }

    // ADD - Ajouter une séance à un emploi du temps existant
    @PostMapping("/{edtId}/seances")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EmploiDuTempsResponseDTO> addSeanceToEdt(
            @PathVariable Long edtId,
            @RequestBody SeanceDTO seanceDTO) {

        EmploiDuTemps edt = emploiDuTempsService.addSeanceToEdt(edtId, seanceDTO);
        return ResponseEntity.ok(mapEdtToDto(edt));
    }

    // GET ALL - Récupérer tous les emplois du temps
    @GetMapping
    public List<EmploiDuTempsResponseDTO> getAllEmploiDuTemps() {
        return emploiDuTempsRepository.findAll().stream()
                .map(this::mapEdtToDto)
                .collect(Collectors.toList());
    }

    // GET BY ID - Récupérer un emploi du temps spécifique
    @GetMapping("/{id}")
    public ResponseEntity<EmploiDuTempsResponseDTO> getEmploiDuTempsById(@PathVariable Long id) {
        return emploiDuTempsRepository.findById(id)
                .map(e -> ResponseEntity.ok(mapEdtToDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Supprimer un emploi du temps
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteEmploiDuTemps(@PathVariable Long id) {
        emploiDuTempsService.deleteEmploiDuTemps(id);
        return ResponseEntity.noContent().build();
    }

    // REMOVE - Retirer une séance d'un emploi du temps
    @DeleteMapping("/{edtId}/seances/{seanceId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EmploiDuTempsResponseDTO> removeSeanceFromEdt(
            @PathVariable Long edtId,
            @PathVariable Long seanceId) {

        EmploiDuTemps edt = emploiDuTempsService.removeSeanceFromEdt(edtId, seanceId);
        return ResponseEntity.ok(mapEdtToDto(edt));
    }

    // POST (recommandé) : génère 16 séances - SEULEMENT POUR ADMINS
    @PostMapping("/generate-weekly-16")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EmploiDuTempsResponseDTO> generateWeekly16Post(
            @RequestParam Long classeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) {
        EmploiDuTemps edt = emploiDuTempsService.generateWeekly16(classeId, weekStart);
        EmploiDuTempsResponseDTO dto = mapEdtToDto(edt);
        return ResponseEntity.created(URI.create("/api/emploi-du-temps/" + edt.getId())).body(dto);
    }

    // GET (optionnel) : même action - SEULEMENT POUR ADMINS
    @GetMapping("/generate-weekly-16")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EmploiDuTempsResponseDTO> generateWeekly16Get(
            @RequestParam Long classeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) {
        EmploiDuTemps edt = emploiDuTempsService.generateWeekly16(classeId, weekStart);
        return ResponseEntity.ok(mapEdtToDto(edt));
    }

    // 👉 Récupérer le DERNIER EDT d'une classe (accessible à tous les utilisateurs authentifiés)
    @GetMapping("/by-classe/{classeId}/latest")
    public ResponseEntity<EmploiDuTempsResponseDTO> getLatestByClasse(@PathVariable Long classeId) {
        Optional<EmploiDuTemps> latest = emploiDuTempsRepository.findTopByClasseIdOrderByDateDebutDesc(classeId);
        return latest.map(e -> ResponseEntity.ok(mapEdtToDto(e)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /* ======= Mapper entité → DTO ======= */
    private EmploiDuTempsResponseDTO mapEdtToDto(EmploiDuTemps edt) {
        EmploiDuTempsResponseDTO dto = new EmploiDuTempsResponseDTO();
        dto.id = edt.getId();
        dto.intitule = edt.getIntitule();
        dto.dateDebut = edt.getDateDebut();
        dto.dateFin = edt.getDateFin();
        dto.actif = edt.isActif();

        // Classe
        if (edt.getClasse() != null) {
            EmploiDuTempsResponseDTO.ClasseLiteDTO cd = new EmploiDuTempsResponseDTO.ClasseLiteDTO();
            cd.id = edt.getClasse().getId();
            cd.nom = edt.getClasse().getNom();
            cd.niveau = edt.getClasse().getNiveau();
            dto.classe = cd;
        }

        // Seances (tri par date + heure)
        dto.seances = edt.getSeances().stream()
                .sorted(Comparator
                        .comparing(Seance::getDate)
                        .thenComparing(Seance::getHeureDebut))
                .map(s -> {
                    EmploiDuTempsResponseDTO.SeanceResponseDTO sd = new EmploiDuTempsResponseDTO.SeanceResponseDTO();
                    sd.id = s.getId();
                    sd.date = s.getDate();
                    sd.heureDebut = s.getHeureDebut();
                    sd.heureFin = s.getHeureFin();
                    sd.statut = s.getStatut() != null ? s.getStatut().name() : null;

                    // Cours
                    if (s.getCours() != null) {
                        EmploiDuTempsResponseDTO.CoursLiteDTO c = new EmploiDuTempsResponseDTO.CoursLiteDTO();
                        c.id = s.getCours().getId();
                        c.code = s.getCours().getCode();
                        c.intitule = s.getCours().getIntitule();
                        sd.cours = c;
                    }

                    // Professeur
                    if (s.getProfesseur() != null) {
                        EmploiDuTempsResponseDTO.ProfesseurLiteDTO p = new EmploiDuTempsResponseDTO.ProfesseurLiteDTO();
                        p.id = s.getProfesseur().getId();
                        p.nom = s.getProfesseur().getNom();
                        p.prenom = s.getProfesseur().getPrenom();
                        sd.professeur = p;
                    }

                    // Salle (si tu as 'code' ou 'nom' dans l'entité Salle)
                    if (s.getSalle() != null) {
                        EmploiDuTempsResponseDTO.SalleLiteDTO sl = new EmploiDuTempsResponseDTO.SalleLiteDTO();
                        sl.id = s.getSalle().getId();
                        try {
                            // si tu as un champ 'code' dans Salle
                            var m = s.getSalle().getClass().getMethod("getCode");
                            sl.code = (String) m.invoke(s.getSalle());
                        } catch (Exception ignored) { }
                        try {
                            // si tu as un champ 'nom' dans Salle
                            var m = s.getSalle().getClass().getMethod("getNom");
                            sl.nom = (String) m.invoke(s.getSalle());
                        } catch (Exception ignored) { }
                        sd.salle = sl;
                    }

                    return sd;
                })
                .collect(Collectors.toList());

        return dto;
    }
}