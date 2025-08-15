package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.entity.*;
import com.example.projet_gestion_absences.repository.*;
import com.example.projet_gestion_absences.model.dto.CoursDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoursService {

    private final CoursRepository coursRepository;
    private final MatiereRepository matiereRepository;

    public CoursService(CoursRepository coursRepository,
                        MatiereRepository matiereRepository) {
        this.coursRepository = coursRepository;
        this.matiereRepository = matiereRepository;
    }

    // Create
    public Cours createCours(CoursDTO coursDTO) {
        Matiere matiere = matiereRepository.findById(coursDTO.getMatiereId())
                .orElseThrow(() -> new RuntimeException("Matiere not found with id: " + coursDTO.getMatiereId()));

        Cours cours = new Cours();
        cours.setCode(coursDTO.getCode());
        cours.setIntitule(coursDTO.getIntitule());
        cours.setDescription(coursDTO.getDescription());
        cours.setCoefficient(coursDTO.getCoefficient());
        cours.setVolumeHoraire(coursDTO.getVolumeHoraire());
        cours.setMatiere(matiere);

        return coursRepository.save(cours);
    }

    // Read
    public List<Cours> getAllCours() {
        return coursRepository.findAll();
    }

    public Optional<Cours> getCoursById(Long id) {
        return coursRepository.findById(id);
    }

    public List<Cours> getCoursByMatiere(Long matiereId) {
        return coursRepository.findByMatiereId(matiereId);
    }

    public List<Cours> searchByIntitule(String intitule) {
        return coursRepository.findByIntituleContainingIgnoreCase(intitule);
    }

    public Cours getCoursByCode(String code) {
        return coursRepository.findByCode(code).stream()
                .findFirst()
                .orElse(null);
    }

    // Update
    public Cours updateCours(Long id, CoursDTO coursDTO) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours not found with id: " + id));

        if (coursDTO.getCode() != null) {
            cours.setCode(coursDTO.getCode());
        }
        if (coursDTO.getIntitule() != null) {
            cours.setIntitule(coursDTO.getIntitule());
        }
        if (coursDTO.getDescription() != null) {
            cours.setDescription(coursDTO.getDescription());
        }
        if (coursDTO.getCoefficient() != null) {
            cours.setCoefficient(coursDTO.getCoefficient());
        }
        if (coursDTO.getVolumeHoraire() != null) {
            cours.setVolumeHoraire(coursDTO.getVolumeHoraire());
        }
        if (coursDTO.getMatiereId() != null) {
            Matiere matiere = matiereRepository.findById(coursDTO.getMatiereId())
                    .orElseThrow(() -> new RuntimeException("Matiere not found with id: " + coursDTO.getMatiereId()));
            cours.setMatiere(matiere);
        }

        return coursRepository.save(cours);
    }

    // Delete
    public void deleteCours(Long id) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours not found with id: " + id));
        coursRepository.delete(cours);
    }
}