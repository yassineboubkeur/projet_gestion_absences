// src/main/java/com/example/projet_gestion_absences/service/EmploiDuTempsService.java
package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.entity.*;
import com.example.projet_gestion_absences.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Service
@Transactional
public class EmploiDuTempsService {

    private final SeanceRepository seanceRepository;
    private final SalleRepository salleRepository;
    private final CoursRepository coursRepository;
    private final ClasseRepository classeRepository;
    private final EmploiDuTempsRepository emploiDuTempsRepository;

    public EmploiDuTempsService(SeanceRepository seanceRepository,
                                SalleRepository salleRepository,
                                CoursRepository coursRepository,
                                ClasseRepository classeRepository,
                                EmploiDuTempsRepository emploiDuTempsRepository) {
        this.seanceRepository = seanceRepository;
        this.salleRepository = salleRepository;
        this.coursRepository = coursRepository;
        this.classeRepository = classeRepository;
        this.emploiDuTempsRepository = emploiDuTempsRepository;
    }

    /**
     * Génère 16 séances sur la semaine [lundi..vendredi] à partir de weekStart (lundi recommandé).
     * Répartition équilibrée: 16 = 3*5 + 1 → quotas [4,3,3,3,3] sur Lun..Ven.
     * Créneaux fixes: 08-10, 10-12, 14-16, 16-18.
     */
    public EmploiDuTemps generateWeekly16(Long classeId, LocalDate weekStart) {
        if (weekStart == null) throw new IllegalArgumentException("weekStart est obligatoire");
        // Aligner sur le lundi si besoin
        LocalDate monday = weekStart.minusDays(weekStart.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());

        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable: " + classeId));

        List<Cours> coursList = coursRepository.findByClasseId(classeId);
        if (coursList.isEmpty()) {
            throw new IllegalStateException("Aucun cours trouvé pour la classe: " + classe.getNom());
        }

        // Persister un EDT pour obtenir un id et cascader les séances
        EmploiDuTemps edt = new EmploiDuTemps(
                "EDT " + classe.getNom() + " (" + monday + " → " + monday.plusDays(6) + ")",
                classe, monday, monday.plusDays(6)
        );
        edt = emploiDuTempsRepository.saveAndFlush(edt);
// juste après avoir récupéré/persisté 'edt'
        if (edt.getSeances() == null) {
            edt.setSeances(new java.util.ArrayList<>());
        }

        // Créneaux fixes (2h chacun)
        LocalTime[] starts = {
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                LocalTime.of(14, 0),
                LocalTime.of(16, 0)
        };

        // Quotas journaliers pour 16 séances sur 5 jours: [4,3,3,3,3]
        int[] dayQuotas = {4, 3, 3, 3, 3};

        // (Option) exiger une salle suffisante pour l'effectif de la classe
        Long effectif = classeRepository.countEtudiantsByClasseId(classeId); // peut être null -> gérer par défaut
        int minCapacite = effectif == null ? 0 : effectif.intValue();

        int scheduled = 0;
        int courseIndex = 0;

        for (int di = 0; di < 5; di++) {
            LocalDate date = monday.plusDays(di);
            int placedToday = 0;

            // On itère les 4 créneaux, on arrête lorsqu'on atteint le quota du jour
            for (LocalTime start : starts) {
                if (placedToday >= dayQuotas[di] || scheduled >= 16) break;
                LocalTime end = start.plusHours(2);

                // Round-robin des cours pour varier les matières
                Cours cours = coursList.get(courseIndex % coursList.size());
                courseIndex++;

                // Un cours doit avoir un prof assigné
                Professeur prof = cours.getProfesseur();
                if (prof == null) continue;

                // Prof libre ?
                boolean profLibre = seanceRepository
                        .findByProfesseurAndDateAndHeureDebutLessThanAndHeureFinGreaterThan(
                                prof, date, end, start
                        ).isEmpty();
                if (!profLibre) continue; // conflit prof

                // Classe libre ?
                boolean classeLibre = !seanceRepository.classeHasOverlap(
                        classeId, date, start, end
                );
                if (!classeLibre) continue; // conflit classe

                // Salles disponibles sur le créneau
                List<Salle> libres = salleRepository.findAvailableSalles(date, start, end);
                if (libres.isEmpty()) continue;

                // Filtrer par capacité si on a un effectif connu
                Salle salleChoisie = libres.stream()
                        .filter(s -> s.getCapacite() >= minCapacite)
                        .findFirst()
                        .orElse(libres.get(0)); // sinon, on prend la 1ère dispo

                // Construire la séance et l'attacher à l'EDT (cascade)
                Seance s = new Seance();
                s.setCours(cours);
                s.setProfesseur(prof);
                s.setSalle(salleChoisie);
                s.setDate(date);
                s.setHeureDebut(start);
                s.setHeureFin(end);
                s.setStatut(Seance.StatutSeance.PLANIFIEE);

                edt.ajouterSeance(s); // remplira s.setEmploiDuTemps(this)

                placedToday++;
                scheduled++;
            }

            if (scheduled >= 16) break;
        }

        // Sauver l’EDT avec ses séances en cascade
        edt = emploiDuTempsRepository.save(edt);
        return emploiDuTempsRepository.findById(edt.getId()).orElseThrow();
    }
}
