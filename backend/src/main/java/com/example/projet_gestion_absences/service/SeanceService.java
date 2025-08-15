package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.entity.*;
import com.example.projet_gestion_absences.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

    // Create
    public Seance createSeance(com.example.projet_gestion_absences.model.dto.SeanceDTO seanceDTO) {
        Cours cours = coursRepository.findById(seanceDTO.getCoursId())
                .orElseThrow(() -> new RuntimeException("Cours not found"));
        Professeur professeur = professeurRepository.findById(seanceDTO.getProfesseurId())
                .orElseThrow(() -> new RuntimeException("Professeur not found"));
        Salle salle = salleRepository.findById(seanceDTO.getSalleId())
                .orElseThrow(() -> new RuntimeException("Salle not found"));

        Seance seance = new Seance();
        seance.setCours(cours);
        seance.setProfesseur(professeur);
        seance.setSalle(salle);
        seance.setDate(seanceDTO.getDate());
        seance.setHeureDebut(seanceDTO.getHeureDebut());
        seance.setHeureFin(seanceDTO.getHeureFin());
        seance.setStatut(seanceDTO.getStatut());

        return seanceRepository.save(seance);
    }

    // Read
    public List<Seance> getAllSeances() {
        return seanceRepository.findAll();
    }

    public Optional<Seance> getSeanceById(Long id) {
        return seanceRepository.findById(id);
    }

    public List<Seance> getSeancesByDate(LocalDate date) {
        return seanceRepository.findByDate(date);
    }

    public List<Seance> getSeancesByProfesseur(Long professeurId) {
        return seanceRepository.findByProfesseurId(professeurId);
    }

    public List<Seance> getSeancesByCours(Long coursId) {
        return seanceRepository.findByCoursId(coursId);
    }

    public List<Seance> getSeancesBySalle(Long salleId) {
        return seanceRepository.findBySalleId(salleId);
    }

    // Update
    public Seance updateSeance(Long id, com.example.projet_gestion_absences.model.dto.SeanceDTO seanceDTO) {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seance not found with id: " + id));

        if (seanceDTO.getCoursId() != null) {
            Cours cours = coursRepository.findById(seanceDTO.getCoursId())
                    .orElseThrow(() -> new RuntimeException("Cours not found"));
            seance.setCours(cours);
        }

        if (seanceDTO.getProfesseurId() != null) {
            Professeur professeur = professeurRepository.findById(seanceDTO.getProfesseurId())
                    .orElseThrow(() -> new RuntimeException("Professeur not found"));
            seance.setProfesseur(professeur);
        }

        if (seanceDTO.getSalleId() != null) {
            Salle salle = salleRepository.findById(seanceDTO.getSalleId())
                    .orElseThrow(() -> new RuntimeException("Salle not found"));
            seance.setSalle(salle);
        }

        if (seanceDTO.getDate() != null) {
            seance.setDate(seanceDTO.getDate());
        }

        if (seanceDTO.getHeureDebut() != null) {
            seance.setHeureDebut(seanceDTO.getHeureDebut());
        }

        if (seanceDTO.getHeureFin() != null) {
            seance.setHeureFin(seanceDTO.getHeureFin());
        }

        if (seanceDTO.getStatut() != null) {
            seance.setStatut(seanceDTO.getStatut());
        }

        return seanceRepository.save(seance);
    }

    // Delete
    public void deleteSeance(Long id) {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seance not found with id: " + id));
        seanceRepository.delete(seance);
    }

    // Check for schedule conflicts
    public boolean hasScheduleConflict(com.example.projet_gestion_absences.model.dto.SeanceDTO seanceDTO) {
        List<Seance> existingSeances = seanceRepository.findByDateAndSalleId(
                seanceDTO.getDate(), seanceDTO.getSalleId());

        LocalTime newStart = seanceDTO.getHeureDebut();
        LocalTime newEnd = seanceDTO.getHeureFin();

        return existingSeances.stream().anyMatch(existing ->
                existing.getHeureDebut().isBefore(newEnd) &&
                        existing.getHeureFin().isAfter(newStart));
    }
}