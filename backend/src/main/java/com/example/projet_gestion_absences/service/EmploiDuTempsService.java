package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.exception.ResourceNotFoundException;
import com.example.projet_gestion_absences.model.dto.SeanceDTO;
import com.example.projet_gestion_absences.model.entity.Classe;
import com.example.projet_gestion_absences.model.entity.Cours;
import com.example.projet_gestion_absences.model.entity.EmploiDuTemps;
import com.example.projet_gestion_absences.model.entity.Professeur;
import com.example.projet_gestion_absences.model.entity.Salle;
import com.example.projet_gestion_absences.model.entity.Seance;
import com.example.projet_gestion_absences.repository.ClasseRepository;
import com.example.projet_gestion_absences.repository.CoursRepository;
import com.example.projet_gestion_absences.repository.EmploiDuTempsRepository;
import com.example.projet_gestion_absences.repository.ProfesseurRepository;
import com.example.projet_gestion_absences.repository.SalleRepository;
import com.example.projet_gestion_absences.repository.SeanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class EmploiDuTempsService {

    private final SeanceRepository seanceRepository;
    private final SalleRepository salleRepository;
    private final CoursRepository coursRepository;
    private final ClasseRepository classeRepository;
    private final EmploiDuTempsRepository emploiDuTempsRepository;
    private final ProfesseurRepository professeurRepository;

    public EmploiDuTempsService(SeanceRepository seanceRepository,
                                SalleRepository salleRepository,
                                CoursRepository coursRepository,
                                ClasseRepository classeRepository,
                                EmploiDuTempsRepository emploiDuTempsRepository,
                                ProfesseurRepository professeurRepository) {
        this.seanceRepository = seanceRepository;
        this.salleRepository = salleRepository;
        this.coursRepository = coursRepository;
        this.classeRepository = classeRepository;
        this.emploiDuTempsRepository = emploiDuTempsRepository;
        this.professeurRepository = professeurRepository;
    }

    // ===================== CRUD / MANUEL =====================

    @Transactional
    public EmploiDuTemps createEmploiDuTemps(String intitule, Long classeId, LocalDate dateDebut, LocalDate dateFin) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable: " + classeId));
        EmploiDuTemps edt = new EmploiDuTemps(intitule, classe, dateDebut, dateFin);
        return emploiDuTempsRepository.save(edt);
    }

    @Transactional
    public EmploiDuTemps addSeanceToEdt(Long edtId, SeanceDTO seanceDTO) {
        EmploiDuTemps edt = emploiDuTempsRepository.findById(edtId)
                .orElseThrow(() -> new IllegalArgumentException("EDT introuvable: " + edtId));

        // Vérifier les conflits avant d'ajouter
        if (hasScheduleConflict(seanceDTO)) {
            throw new IllegalStateException("Conflit d'horaire détecté");
        }

        Seance seance = new Seance();

        // Mapping des propriétés depuis seanceDTO
        if (seanceDTO.getCoursId() != null) {
            Cours cours = coursRepository.findById(seanceDTO.getCoursId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cours introuvable"));
            seance.setCours(cours);
        }
        if (seanceDTO.getProfesseurId() != null) {
            Professeur professeur = professeurRepository.findById(seanceDTO.getProfesseurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Professeur introuvable"));
            seance.setProfesseur(professeur);
        }
        if (seanceDTO.getSalleId() != null) {
            Salle salle = salleRepository.findById(seanceDTO.getSalleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Salle introuvable"));
            seance.setSalle(salle);
        }

        seance.setDate(seanceDTO.getDate());
        seance.setHeureDebut(seanceDTO.getHeureDebut());
        seance.setHeureFin(seanceDTO.getHeureFin());
        seance.setStatut(seanceDTO.getStatut());
        seance.setEmploiDuTemps(edt);

        edt.ajouterSeance(seance);
        return emploiDuTempsRepository.save(edt);
    }

    @Transactional
    public EmploiDuTemps removeSeanceFromEdt(Long edtId, Long seanceId) {
        EmploiDuTemps edt = emploiDuTempsRepository.findById(edtId)
                .orElseThrow(() -> new IllegalArgumentException("EDT introuvable: " + edtId));

        Seance seance = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new IllegalArgumentException("Séance introuvable: " + seanceId));

        if (!seance.getEmploiDuTemps().getId().equals(edtId)) {
            throw new IllegalArgumentException("La séance n'appartient pas à cet EDT");
        }

        edt.getSeances().remove(seance);
        seance.setEmploiDuTemps(null);
        seanceRepository.delete(seance);

        return emploiDuTempsRepository.save(edt);
    }

    @Transactional
    public void deleteEmploiDuTemps(Long id) {
        EmploiDuTemps edt = emploiDuTempsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("EDT introuvable: " + id));
        emploiDuTempsRepository.delete(edt);
    }

    // ===================== LATEST BY CLASSE =====================

    /**
     * Renvoie l'EDT le plus récent d'une classe (utilisé pour l'export PDF et le frontend).
     * On se base sur la date de début (la plus récente).
     */
    @Transactional(readOnly = true)
    public EmploiDuTemps getLatestByClasseId(Long classeId) {
        return emploiDuTempsRepository
                .findTopByClasseIdOrderByDateDebutDesc(classeId)
                .orElse(null);
    }

    /**
     * Variante "orThrow" si tu préfères forcer l'existence.
     */
    @Transactional(readOnly = true)
    public EmploiDuTemps getLatestByClasseIdOrThrow(Long classeId) {
        return emploiDuTempsRepository
                .findTopByClasseIdOrderByDateDebutDesc(classeId)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun emploi du temps pour la classe: " + classeId));
    }

    // ===================== CONFLITS =====================

    /**
     * Vérifie les conflits d'horaires:
     *  - Salle sur le même créneau
     *  - Professeur sur le même créneau
     *  - Classe (via le cours) sur le même créneau
     * Null-safe: n'évalue que ce qui est renseigné dans le DTO.
     */
    @Transactional(readOnly = true)
    public boolean hasScheduleConflict(SeanceDTO seanceDTO) {
        if (seanceDTO == null || seanceDTO.getDate() == null
                || seanceDTO.getHeureDebut() == null || seanceDTO.getHeureFin() == null) {
            return false; // on laisse la validation métier/REST gérer ces cas
        }

        LocalTime newStart = seanceDTO.getHeureDebut();
        LocalTime newEnd   = seanceDTO.getHeureFin();

        // Conflit salle ?
        if (seanceDTO.getSalleId() != null) {
            List<Seance> existingSeances = seanceRepository.findByDateAndSalleId(
                    seanceDTO.getDate(), seanceDTO.getSalleId());

            boolean salleOverlap = existingSeances.stream().anyMatch(existing ->
                    existing.getHeureDebut().isBefore(newEnd) &&
                            existing.getHeureFin().isAfter(newStart)
            );
            if (salleOverlap) return true;
        }

        // Conflit professeur ?
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

        // Conflit classe (via cours -> classe) ?
        if (seanceDTO.getCoursId() != null) {
            Cours cours = coursRepository.findById(seanceDTO.getCoursId()).orElse(null);
            if (cours != null && cours.getClasse() != null) {
                boolean classeOverlap = seanceRepository.classeHasOverlap(
                        cours.getClasse().getId(),
                        seanceDTO.getDate(),
                        newStart,
                        newEnd
                );
                if (classeOverlap) return true;
            }
        }

        return false;
    }

    // ===================== GÉNÉRATION AUTOMATIQUE (existant) =====================

    public EmploiDuTemps generateWeekly16(Long classeId, LocalDate weekStart) {
        if (weekStart == null) throw new IllegalArgumentException("weekStart est obligatoire");

        // Aligner sur le lundi
        LocalDate monday = weekStart.minusDays(weekStart.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());

        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable: " + classeId));

        List<Cours> coursList = coursRepository.findByClasseId(classeId);
        if (coursList.isEmpty()) {
            throw new IllegalStateException("Aucun cours trouvé pour la classe: " + classe.getNom());
        }

        // Créer/flush pour obtenir un id et cascader les séances
        EmploiDuTemps edt = new EmploiDuTemps(
                "EDT " + classe.getNom() + " (" + monday + " → " + monday.plusDays(6) + ")",
                classe, monday, monday.plusDays(6)
        );
        edt = emploiDuTempsRepository.saveAndFlush(edt);

        if (edt.getSeances() == null) {
            edt.setSeances(new java.util.ArrayList<>());
        }

        // Créneaux fixes (2h)
        LocalTime[] starts = {
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                LocalTime.of(14, 0),
                LocalTime.of(16, 0)
        };

        // Quotas: 16 séances / 5 jours
        int[] dayQuotas = {4, 3, 3, 3, 3};

        Long effectif = classeRepository.countEtudiantsByClasseId(classeId);
        int minCapacite = effectif == null ? 0 : effectif.intValue();

        int scheduled = 0;
        int courseIndex = 0;

        for (int di = 0; di < 5; di++) {
            LocalDate date = monday.plusDays(di);
            int placedToday = 0;

            for (LocalTime start : starts) {
                if (placedToday >= dayQuotas[di] || scheduled >= 16) break;
                LocalTime end = start.plusHours(2);

                Cours cours = coursList.get(courseIndex % coursList.size());
                courseIndex++;

                Professeur prof = cours.getProfesseur();
                if (prof == null) continue;

                boolean profLibre = seanceRepository
                        .findByProfesseurAndDateAndHeureDebutLessThanAndHeureFinGreaterThan(
                                prof, date, end, start
                        ).isEmpty();
                if (!profLibre) continue;

                boolean classeLibre = !seanceRepository.classeHasOverlap(
                        classeId, date, start, end
                );
                if (!classeLibre) continue;

                List<Salle> libres = salleRepository.findAvailableSalles(date, start, end);
                if (libres.isEmpty()) continue;

                Salle salleChoisie = libres.stream()
                        .filter(s -> s.getCapacite() >= minCapacite)
                        .findFirst()
                        .orElse(libres.get(0));

                Seance s = new Seance();
                s.setCours(cours);
                s.setProfesseur(prof);
                s.setSalle(salleChoisie);
                s.setDate(date);
                s.setHeureDebut(start);
                s.setHeureFin(end);
                s.setStatut(Seance.StatutSeance.PLANIFIEE);

                edt.ajouterSeance(s);

                placedToday++;
                scheduled++;
            }
            if (scheduled >= 16) break;
        }

        edt = emploiDuTempsRepository.save(edt);

        // (optionnel) tri des séances avant retour
        edt.getSeances().sort(
                Comparator.comparing(Seance::getDate)
                        .thenComparing(Seance::getHeureDebut)
        );

        return emploiDuTempsRepository.findById(edt.getId()).orElseThrow();
    }
}


//package com.example.projet_gestion_absences.service;
//
//import com.example.projet_gestion_absences.model.dto.SeanceDTO;
//import com.example.projet_gestion_absences.model.entity.*;
//import com.example.projet_gestion_absences.repository.*;
//import com.example.projet_gestion_absences.exception.ResourceNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//@Service
//@Transactional
//public class EmploiDuTempsService {
//
//    private final SeanceRepository seanceRepository;
//    private final SalleRepository salleRepository;
//    private final CoursRepository coursRepository;
//    private final ClasseRepository classeRepository;
//    private final EmploiDuTempsRepository emploiDuTempsRepository;
//    private final ProfesseurRepository professeurRepository;
//
//    public EmploiDuTempsService(SeanceRepository seanceRepository,
//                                SalleRepository salleRepository,
//                                CoursRepository coursRepository,
//                                ClasseRepository classeRepository,
//                                EmploiDuTempsRepository emploiDuTempsRepository,
//                                ProfesseurRepository professeurRepository) {
//        this.seanceRepository = seanceRepository;
//        this.salleRepository = salleRepository;
//        this.coursRepository = coursRepository;
//        this.classeRepository = classeRepository;
//        this.emploiDuTempsRepository = emploiDuTempsRepository;
//        this.professeurRepository = professeurRepository;
//    }
//
//    // NOUVELLES MÉTHODES POUR GESTION MANUELLE
//
//    @Transactional
//    public EmploiDuTemps createEmploiDuTemps(String intitule, Long classeId, LocalDate dateDebut, LocalDate dateFin) {
//        Classe classe = classeRepository.findById(classeId)
//                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable: " + classeId));
//
//        EmploiDuTemps edt = new EmploiDuTemps(intitule, classe, dateDebut, dateFin);
//        return emploiDuTempsRepository.save(edt);
//    }
//
//    @Transactional
//    public EmploiDuTemps addSeanceToEdt(Long edtId, SeanceDTO seanceDTO) {
//        EmploiDuTemps edt = emploiDuTempsRepository.findById(edtId)
//                .orElseThrow(() -> new IllegalArgumentException("EDT introuvable: " + edtId));
//
//        // Vérifier les conflits avant d'ajouter
//        if (hasScheduleConflict(seanceDTO)) {
//            throw new IllegalStateException("Conflit d'horaire détecté");
//        }
//
//        // Créer la séance
//        Seance seance = new Seance();
//
//        // Mapping des propriétés depuis seanceDTO
//        if (seanceDTO.getCoursId() != null) {
//            Cours cours = coursRepository.findById(seanceDTO.getCoursId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Cours introuvable"));
//            seance.setCours(cours);
//        }
//        if (seanceDTO.getProfesseurId() != null) {
//            Professeur professeur = professeurRepository.findById(seanceDTO.getProfesseurId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Professeur introuvable"));
//            seance.setProfesseur(professeur);
//        }
//        if (seanceDTO.getSalleId() != null) {
//            Salle salle = salleRepository.findById(seanceDTO.getSalleId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Salle introuvable"));
//            seance.setSalle(salle);
//        }
//
//        seance.setDate(seanceDTO.getDate());
//        seance.setHeureDebut(seanceDTO.getHeureDebut());
//        seance.setHeureFin(seanceDTO.getHeureFin());
//        seance.setStatut(seanceDTO.getStatut());
//        seance.setEmploiDuTemps(edt);
//
//        edt.ajouterSeance(seance);
//        return emploiDuTempsRepository.save(edt);
//    }
//
//    @Transactional
//    public EmploiDuTemps removeSeanceFromEdt(Long edtId, Long seanceId) {
//        EmploiDuTemps edt = emploiDuTempsRepository.findById(edtId)
//                .orElseThrow(() -> new IllegalArgumentException("EDT introuvable: " + edtId));
//
//        Seance seance = seanceRepository.findById(seanceId)
//                .orElseThrow(() -> new IllegalArgumentException("Séance introuvable: " + seanceId));
//
//        if (!seance.getEmploiDuTemps().getId().equals(edtId)) {
//            throw new IllegalArgumentException("La séance n'appartient pas à cet EDT");
//        }
//
//        edt.getSeances().remove(seance);
//        seance.setEmploiDuTemps(null);
//        seanceRepository.delete(seance);
//
//        return emploiDuTempsRepository.save(edt);
//    }
//
//    @Transactional
//    public void deleteEmploiDuTemps(Long id) {
//        EmploiDuTemps edt = emploiDuTempsRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("EDT introuvable: " + id));
//        emploiDuTempsRepository.delete(edt);
//    }
//
//    // Méthode pour vérifier les conflits (similaire à celle dans SeanceService)
//    private boolean hasScheduleConflict(SeanceDTO seanceDTO) {
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
//
//    // MÉTHODE EXISTANTE POUR GÉNÉRATION AUTOMATIQUE
//    public EmploiDuTemps generateWeekly16(Long classeId, LocalDate weekStart) {
//        // ... code existant pour la génération automatique
//        if (weekStart == null) throw new IllegalArgumentException("weekStart est obligatoire");
//        // Aligner sur le lundi si besoin
//        LocalDate monday = weekStart.minusDays(weekStart.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
//
//        Classe classe = classeRepository.findById(classeId)
//                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable: " + classeId));
//
//        List<Cours> coursList = coursRepository.findByClasseId(classeId);
//        if (coursList.isEmpty()) {
//            throw new IllegalStateException("Aucun cours trouvé pour la classe: " + classe.getNom());
//        }
//
//        // Persister un EDT pour obtenir un id et cascader les séances
//        EmploiDuTemps edt = new EmploiDuTemps(
//                "EDT " + classe.getNom() + " (" + monday + " → " + monday.plusDays(6) + ")",
//                classe, monday, monday.plusDays(6)
//        );
//        edt = emploiDuTempsRepository.saveAndFlush(edt);
//// juste après avoir récupéré/persisté 'edt'
//        if (edt.getSeances() == null) {
//            edt.setSeances(new java.util.ArrayList<>());
//        }
//
//        // Créneaux fixes (2h chacun)
//        LocalTime[] starts = {
//                LocalTime.of(8, 0),
//                LocalTime.of(10, 0),
//                LocalTime.of(14, 0),
//                LocalTime.of(16, 0)
//        };
//
//        // Quotas journaliers pour 16 séances sur 5 jours: [4,3,3,3,3]
//        int[] dayQuotas = {4, 3, 3, 3, 3};
//
//        // (Option) exiger une salle suffisante pour l'effectif de la classe
//        Long effectif = classeRepository.countEtudiantsByClasseId(classeId); // peut être null -> gérer par défaut
//        int minCapacite = effectif == null ? 0 : effectif.intValue();
//
//        int scheduled = 0;
//        int courseIndex = 0;
//
//        for (int di = 0; di < 5; di++) {
//            LocalDate date = monday.plusDays(di);
//            int placedToday = 0;
//
//            // On itère les 4 créneaux, on arrête lorsqu'on atteint le quota du jour
//            for (LocalTime start : starts) {
//                if (placedToday >= dayQuotas[di] || scheduled >= 16) break;
//                LocalTime end = start.plusHours(2);
//
//                // Round-robin des cours pour varier les matières
//                Cours cours = coursList.get(courseIndex % coursList.size());
//                courseIndex++;
//
//                // Un cours doit avoir un prof assigné
//                Professeur prof = cours.getProfesseur();
//                if (prof == null) continue;
//
//                // Prof libre ?
//                boolean profLibre = seanceRepository
//                        .findByProfesseurAndDateAndHeureDebutLessThanAndHeureFinGreaterThan(
//                                prof, date, end, start
//                        ).isEmpty();
//                if (!profLibre) continue; // conflit prof
//
//                // Classe libre ?
//                boolean classeLibre = !seanceRepository.classeHasOverlap(
//                        classeId, date, start, end
//                );
//                if (!classeLibre) continue; // conflit classe
//
//                // Salles disponibles sur le créneau
//                List<Salle> libres = salleRepository.findAvailableSalles(date, start, end);
//                if (libres.isEmpty()) continue;
//
//                // Filtrer par capacité si on a un effectif connu
//                Salle salleChoisie = libres.stream()
//                        .filter(s -> s.getCapacite() >= minCapacite)
//                        .findFirst()
//                        .orElse(libres.get(0)); // sinon, on prend la 1ère dispo
//
//                // Construire la séance et l'attacher à l'EDT (cascade)
//                Seance s = new Seance();
//                s.setCours(cours);
//                s.setProfesseur(prof);
//                s.setSalle(salleChoisie);
//                s.setDate(date);
//                s.setHeureDebut(start);
//                s.setHeureFin(end);
//                s.setStatut(Seance.StatutSeance.PLANIFIEE);
//
//                edt.ajouterSeance(s); // remplira s.setEmploiDuTemps(this)
//
//                placedToday++;
//                scheduled++;
//            }
//
//            if (scheduled >= 16) break;
//        }
//
//        // Sauver l’EDT avec ses séances en cascade
//        edt = emploiDuTempsRepository.save(edt);
//        return emploiDuTempsRepository.findById(edt.getId()).orElseThrow();
//    }
//}
//
