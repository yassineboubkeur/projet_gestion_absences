package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.exception.ResourceNotFoundException;
import com.example.projet_gestion_absences.model.dto.SeanceDTO;
import com.example.projet_gestion_absences.model.dto.SeanceResponseDTO;
import com.example.projet_gestion_absences.model.entity.*;
import com.example.projet_gestion_absences.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class SeanceService {

    private final SeanceRepository seanceRepository;
    private final CoursRepository coursRepository;
    private final ProfesseurRepository professeurRepository;
    private final SalleRepository salleRepository;

    public SeanceService(SeanceRepository seanceRepository,
                         CoursRepository coursRepository,
                         ProfesseurRepository professeurRepository,
                         SalleRepository salleRepository) {
        this.seanceRepository = seanceRepository;
        this.coursRepository = coursRepository;
        this.professeurRepository = professeurRepository;
        this.salleRepository = salleRepository;
    }

    /* ----------------------- Helpers ----------------------- */

    private SeanceResponseDTO map(Seance s) {
        return new SeanceResponseDTO(
                s.getId(),
                s.getDate(),
                s.getHeureDebut(),
                s.getHeureFin(),
                s.getStatut() != null ? s.getStatut().name() : null,
                s.getCours() != null ? s.getCours().getId() : null,
                s.getProfesseur() != null ? s.getProfesseur().getId() : null,
                s.getSalle() != null ? s.getSalle().getId() : null,
                s.getEmploiDuTemps() != null ? s.getEmploiDuTemps().getId() : null,
                s.getColor() // <-- NEW
        );
    }

    private Seance buildFromDto(SeanceDTO dto, Seance target) {
        if (dto.getCoursId() != null) {
            Cours cours = coursRepository.findById(dto.getCoursId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cours introuvable id=" + dto.getCoursId()));
            target.setCours(cours);
        }
        if (dto.getProfesseurId() != null) {
            Professeur professeur = professeurRepository.findById(dto.getProfesseurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Professeur introuvable id=" + dto.getProfesseurId()));
            target.setProfesseur(professeur);
        }
        if (dto.getSalleId() != null) {
            Salle salle = salleRepository.findById(dto.getSalleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Salle introuvable id=" + dto.getSalleId()));
            target.setSalle(salle);
        }
        if (dto.getDate() != null)        target.setDate(dto.getDate());
        if (dto.getHeureDebut() != null)  target.setHeureDebut(dto.getHeureDebut());
        if (dto.getHeureFin() != null)    target.setHeureFin(dto.getHeureFin());
        if (dto.getStatut() != null)      target.setStatut(dto.getStatut());
        if (dto.getColor() != null)       target.setColor(dto.getColor()); // <-- NEW

        if (target.getStatut() == null)   target.setStatut(Seance.StatutSeance.PLANIFIEE);

        // Couleur par défaut si absente
        if (target.getColor() == null || target.getColor().isBlank()) {
            target.setColor(generateStableColor(target));
        }

        return target;
    }

    /** Génère une couleur hex stable selon cours/prof/salle (si dispo) */
    private String generateStableColor(Seance s) {
        String key = (s.getCours() != null ? s.getCours().getId() : 0L) + "|" +
                (s.getProfesseur() != null ? s.getProfesseur().getId() : 0L) + "|" +
                (s.getSalle() != null ? s.getSalle().getId() : 0L);
        int h = 0;
        for (int i = 0; i < key.length(); i++) h = (h * 31 + key.charAt(i)) & 0x7fffffff;
        String[] palette = {
                "#FDE68A", "#A7F3D0", "#BFDBFE", "#FBCFE8", "#DDD6FE",
                "#FECACA", "#BBF7D0", "#BAE6FD", "#FEF3C7", "#C7D2FE",
                "#D9F99D", "#A5B4FC", "#F5D0FE", "#FDBA74", "#E9D5FF"
        };
        return palette[h % palette.length];
    }

    /* ----------------------- Create ----------------------- */

    public SeanceResponseDTO createSeance(SeanceDTO seanceDTO) {
        Seance seance = new Seance();
        buildFromDto(seanceDTO, seance);
        Seance saved = seanceRepository.save(seance);
        return map(saved);
    }

    /* ----------------------- Read ----------------------- */

    public List<SeanceResponseDTO> getAllSeances() {
        return seanceRepository.findAll().stream().map(this::map).toList();
    }

    public SeanceResponseDTO getSeanceById(Long id) {
        Seance s = seanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Séance introuvable id=" + id));
        return map(s);
    }

    public List<SeanceResponseDTO> getSeancesByDate(LocalDate date) {
        return seanceRepository.findByDate(date).stream().map(this::map).toList();
    }

    public List<SeanceResponseDTO> getSeancesByProfesseur(Long professeurId) {
        return seanceRepository.findByProfesseurId(professeurId).stream().map(this::map).toList();
    }

    public List<SeanceResponseDTO> getSeancesByCours(Long coursId) {
        return seanceRepository.findByCoursId(coursId).stream().map(this::map).toList();
    }

    public List<SeanceResponseDTO> getSeancesBySalle(Long salleId) {
        return seanceRepository.findBySalleId(salleId).stream().map(this::map).toList();
    }

    /* ----------------------- Update ----------------------- */

    public SeanceResponseDTO updateSeance(Long id, SeanceDTO seanceDTO) {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Séance introuvable id=" + id));

        buildFromDto(seanceDTO, seance);

        Seance saved = seanceRepository.save(seance);
        return map(saved);
    }

    /* ----------------------- Delete ----------------------- */

    public void deleteSeance(Long id) {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Séance introuvable id=" + id));
        seanceRepository.delete(seance);
    }

    /* ----------------- Check for schedule conflicts ----------------- */

    public boolean hasScheduleConflict(SeanceDTO seanceDTO) {
        // Conflit salle (chevauchement même salle / même date)
        List<Seance> existingSeances = seanceRepository.findByDateAndSalleId(
                seanceDTO.getDate(), seanceDTO.getSalleId());

        LocalTime newStart = seanceDTO.getHeureDebut();
        LocalTime newEnd   = seanceDTO.getHeureFin();

        boolean salleOverlap = existingSeances.stream().anyMatch(existing ->
                existing.getHeureDebut().isBefore(newEnd) &&
                        existing.getHeureFin().isAfter(newStart)
        );
        if (salleOverlap) return true;

        // Conflit professeur
        if (seanceDTO.getProfesseurId() != null) {
            Professeur prof = professeurRepository.findById(seanceDTO.getProfesseurId()).orElse(null);
            if (prof != null) {
                boolean profOverlap = !seanceRepository
                        .findByProfesseurAndDateAndHeureDebutLessThanAndHeureFinGreaterThan(
                                prof, seanceDTO.getDate(), newEnd, newStart
                        ).isEmpty();
                if (profOverlap) return true;
            }
        }

        // Conflit classe (via cours -> classe)
        if (seanceDTO.getCoursId() != null) {
            Cours cours = coursRepository.findById(seanceDTO.getCoursId()).orElse(null);
            if (cours != null && cours.getClasse() != null) {
                boolean classeOverlap = seanceRepository.classeHasOverlap(
                        cours.getClasse().getId(),
                        seanceDTO.getDate(),
                        newStart, newEnd
                );
                if (classeOverlap) return true;
            }
        }
        return false;
    }
}


//package com.example.projet_gestion_absences.service;
//
//import com.example.projet_gestion_absences.model.dto.SeanceDTO;
//import com.example.projet_gestion_absences.model.dto.SeanceResponseDTO;
//import com.example.projet_gestion_absences.model.entity.*;
//import com.example.projet_gestion_absences.repository.*;
//import com.example.projet_gestion_absences.exception.ResourceNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//@Service
//@Transactional
//public class SeanceService {
//
//    private final SeanceRepository seanceRepository;
//    private final CoursRepository coursRepository;
//    private final ProfesseurRepository professeurRepository;
//    private final SalleRepository salleRepository;
//
//    public SeanceService(SeanceRepository seanceRepository,
//                         CoursRepository coursRepository,
//                         ProfesseurRepository professeurRepository,
//                         SalleRepository salleRepository) {
//        this.seanceRepository = seanceRepository;
//        this.coursRepository = coursRepository;
//        this.professeurRepository = professeurRepository;
//        this.salleRepository = salleRepository;
//    }
//
//    /* ----------------------- Helpers ----------------------- */
//
//    private SeanceResponseDTO map(Seance s) {
//        return new SeanceResponseDTO(
//                s.getId(),
//                s.getDate(),
//                s.getHeureDebut(),
//                s.getHeureFin(),
//                s.getStatut() != null ? s.getStatut().name() : null,
//                s.getCours() != null ? s.getCours().getId() : null,
//                s.getProfesseur() != null ? s.getProfesseur().getId() : null,
//                s.getSalle() != null ? s.getSalle().getId() : null,
//                s.getEmploiDuTemps() != null ? s.getEmploiDuTemps().getId() : null
//        );
//    }
//
//    private Seance buildFromDto(SeanceDTO dto, Seance target) {
//        if (dto.getCoursId() != null) {
//            Cours cours = coursRepository.findById(dto.getCoursId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Cours introuvable id=" + dto.getCoursId()));
//            target.setCours(cours);
//        }
//        if (dto.getProfesseurId() != null) {
//            Professeur professeur = professeurRepository.findById(dto.getProfesseurId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Professeur introuvable id=" + dto.getProfesseurId()));
//            target.setProfesseur(professeur);
//        }
//        if (dto.getSalleId() != null) {
//            Salle salle = salleRepository.findById(dto.getSalleId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Salle introuvable id=" + dto.getSalleId()));
//            target.setSalle(salle);
//        }
//        if (dto.getDate() != null)        target.setDate(dto.getDate());
//        if (dto.getHeureDebut() != null)  target.setHeureDebut(dto.getHeureDebut());
//        if (dto.getHeureFin() != null)    target.setHeureFin(dto.getHeureFin());
//        if (dto.getStatut() != null)      target.setStatut(dto.getStatut());
//        if (target.getStatut() == null)   target.setStatut(Seance.StatutSeance.PLANIFIEE); // défaut
//
//        return target;
//    }
//
//    /* ----------------------- Create ----------------------- */
//
//    public SeanceResponseDTO createSeance(SeanceDTO seanceDTO) {
//        Seance seance = new Seance();
//        buildFromDto(seanceDTO, seance);
//
//        // (optionnel) Ici tu peux valider les conflits avant save si tu veux empêcher la création
//        // if (hasScheduleConflict(seanceDTO)) { throw new IllegalStateException("Conflit d'horaire"); }
//
//        Seance saved = seanceRepository.save(seance);
//        return map(saved);
//    }
//
//    /* ----------------------- Read ----------------------- */
//
//    public List<SeanceResponseDTO> getAllSeances() {
//        return seanceRepository.findAll().stream().map(this::map).toList();
//    }
//
//    public SeanceResponseDTO getSeanceById(Long id) {
//        Seance s = seanceRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Séance introuvable id=" + id));
//        return map(s);
//    }
//
//    public List<SeanceResponseDTO> getSeancesByDate(LocalDate date) {
//        return seanceRepository.findByDate(date).stream().map(this::map).toList();
//    }
//
//    public List<SeanceResponseDTO> getSeancesByProfesseur(Long professeurId) {
//        return seanceRepository.findByProfesseurId(professeurId).stream().map(this::map).toList();
//    }
//
//    public List<SeanceResponseDTO> getSeancesByCours(Long coursId) {
//        return seanceRepository.findByCoursId(coursId).stream().map(this::map).toList();
//    }
//
//    public List<SeanceResponseDTO> getSeancesBySalle(Long salleId) {
//        return seanceRepository.findBySalleId(salleId).stream().map(this::map).toList();
//    }
//
//    /* ----------------------- Update ----------------------- */
//
//    public SeanceResponseDTO updateSeance(Long id, SeanceDTO seanceDTO) {
//        Seance seance = seanceRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Séance introuvable id=" + id));
//
//        buildFromDto(seanceDTO, seance);
//
//        // (optionnel) revalider conflit après modif
//        // if (hasScheduleConflict(seanceDTO)) { throw new IllegalStateException("Conflit d'horaire"); }
//
//        Seance saved = seanceRepository.save(seance);
//        return map(saved);
//    }
//
//    /* ----------------------- Delete ----------------------- */
//
//    public void deleteSeance(Long id) {
//        Seance seance = seanceRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Séance introuvable id=" + id));
//        seanceRepository.delete(seance);
//    }
//
//    /* ----------------- Check for schedule conflicts ----------------- */
//
//    public boolean hasScheduleConflict(SeanceDTO seanceDTO) {
//        // Conflit salle (chevauchement même salle / même date)
//        List<Seance> existingSeances = seanceRepository.findByDateAndSalleId(
//                seanceDTO.getDate(), seanceDTO.getSalleId());
//
//        LocalTime newStart = seanceDTO.getHeureDebut();
//        LocalTime newEnd   = seanceDTO.getHeureFin();
//
//        boolean salleOverlap = existingSeances.stream().anyMatch(existing ->
//                existing.getHeureDebut().isBefore(newEnd) &&
//                        existing.getHeureFin().isAfter(newStart)
//        );
//
//        if (salleOverlap) return true;
//
//        // Conflit professeur
//        if (seanceDTO.getProfesseurId() != null) {
//            Professeur prof = professeurRepository.findById(seanceDTO.getProfesseurId())
//                    .orElse(null);
//            if (prof != null) {
//                boolean profOverlap = !seanceRepository
//                        .findByProfesseurAndDateAndHeureDebutLessThanAndHeureFinGreaterThan(
//                                prof, seanceDTO.getDate(), newEnd, newStart
//                        ).isEmpty();
//                if (profOverlap) return true;
//            }
//        }
//
//        // Conflit classe (via le cours -> classe)
//        if (seanceDTO.getCoursId() != null) {
//            Cours cours = coursRepository.findById(seanceDTO.getCoursId()).orElse(null);
//            if (cours != null && cours.getClasse() != null) {
//                boolean classeOverlap = seanceRepository.classeHasOverlap(
//                        cours.getClasse().getId(),
//                        seanceDTO.getDate(),
//                        newStart,
//                        newEnd
//                );
//                if (classeOverlap) return true;
//            }
//        }
//
//        return false;
//    }
//}
