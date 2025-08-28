package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.entity.Salle;
import com.example.projet_gestion_absences.repository.SalleRepository;
import com.example.projet_gestion_absences.repository.SeanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class SalleService {

    private final SalleRepository salleRepository;
    private final SeanceRepository seanceRepository;

    public SalleService(SalleRepository salleRepository,
                        SeanceRepository seanceRepository) {
        this.salleRepository = salleRepository;
        this.seanceRepository = seanceRepository;
    }

    // Create
    public Salle createSalle(Salle req) {
        Salle salle = new Salle();
        salle.setCode(req.getCode());
        salle.setBatiment(req.getBatiment());
        salle.setNumero(req.getNumero());
        salle.setCapacite(req.getCapacite());
        salle.setType(req.getType());
        return salleRepository.save(salle);
    }

    // Read
    @Transactional(readOnly = true)
    public List<Salle> getAllSalles() {
        return salleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Salle getSalleById(Long id) {
        return salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Salle getSalleByCode(String code) {
        return salleRepository.findByCode(code);
    }

    // Update
    public Salle updateSalle(Long id, Salle req) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle not found with id: " + id));

        salle.setCode(req.getCode());
        salle.setBatiment(req.getBatiment());
        salle.setNumero(req.getNumero());
        salle.setCapacite(req.getCapacite());
        salle.setType(req.getType());

        return salleRepository.save(salle);
    }

    // Delete
    public void deleteSalle(Long id) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle not found with id: " + id));
        salleRepository.delete(salle);
    }

    // -------- DISPONIBILITÉ (utilisé par /api/salles/available) --------
    /**
     * Renvoie les salles disponibles pour un créneau (date/start/end).
     * Règle de chevauchement: (start < s.heureFin) && (end > s.heureDebut)
     */
    @Transactional(readOnly = true)
    public List<Salle> getAvailable(LocalDate date, LocalTime start, LocalTime end) {
        // IDs de salles occupées sur ce créneau (requête dans SeanceRepository)
        List<Long> busyIds = seanceRepository.findBusySalleIds(date, start, end);

        // Si aucun n'est occupé → toutes les salles sont dispo
        if (busyIds == null || busyIds.isEmpty()) {
            return salleRepository.findAll();
        }

        // Si tous sont occupés → liste vide
        // (évite NOT IN () et gère le cas où busyIds = ALL)
        List<Salle> free = salleRepository.findByIdNotIn(busyIds);
        return free != null ? free : Collections.emptyList();
    }
}
