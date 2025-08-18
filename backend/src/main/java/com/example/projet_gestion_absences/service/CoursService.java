package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.dto.CoursDTO;
import com.example.projet_gestion_absences.model.entity.Classe;
import com.example.projet_gestion_absences.model.entity.Cours;
import com.example.projet_gestion_absences.model.entity.Matiere;
import com.example.projet_gestion_absences.model.entity.Professeur;
import com.example.projet_gestion_absences.repository.ClasseRepository;
import com.example.projet_gestion_absences.repository.CoursRepository;
import com.example.projet_gestion_absences.repository.MatiereRepository;
import com.example.projet_gestion_absences.repository.ProfesseurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoursService {

    private final CoursRepository coursRepository;
    private final MatiereRepository matiereRepository;
    private final ClasseRepository classeRepository;
    private final ProfesseurRepository professeurRepository;

    public CoursService(CoursRepository coursRepository,
                        MatiereRepository matiereRepository,
                        ClasseRepository classeRepository,
                        ProfesseurRepository professeurRepository) {
        this.coursRepository = coursRepository;
        this.matiereRepository = matiereRepository;
        this.classeRepository = classeRepository;
        this.professeurRepository = professeurRepository;
    }

    /* =========================
       Helpers d'affectation
       ========================= */

    public Cours setProfesseurForCours(Long coursId, Long professeurId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours not found with id: " + coursId));
        Professeur prof = professeurRepository.findById(professeurId)
                .orElseThrow(() -> new RuntimeException("Professeur not found with id: " + professeurId));
        cours.setProfesseur(prof);
        return coursRepository.save(cours);
    }

    public Cours setClasseForCours(Long coursId, Long classeId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours not found with id: " + coursId));
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new RuntimeException("Classe not found with id: " + classeId));
        cours.setClasse(classe);
        return coursRepository.save(cours);
    }

    /* =========================
       Read
       ========================= */

    public List<Cours> getAllCours() {
        return coursRepository.findAll();
    }

    public Optional<Cours> getCoursById(Long id) {
        return coursRepository.findById(id);
    }

    public List<Cours> getCoursByMatiere(Long matiereId) {
        return coursRepository.findByMatiereId(matiereId);
    }

    public List<Cours> getCoursByClasse(Long classeId) {
        return coursRepository.findByClasseId(classeId);
    }

    public List<Cours> searchByIntitule(String intitule) {
        return coursRepository.findByIntituleContainingIgnoreCase(intitule);
    }

    public Cours getCoursByCode(String code) {
        return coursRepository.findByCode(code).stream().findFirst().orElse(null);
    }

    /* =========================
       Create
       ========================= */

    public Cours createCours(CoursDTO dto) {
        Matiere matiere = matiereRepository.findById(dto.getMatiereId())
                .orElseThrow(() -> new RuntimeException("Matiere not found with id: " + dto.getMatiereId()));

        Cours cours = new Cours();
        cours.setCode(dto.getCode());
        cours.setIntitule(dto.getIntitule());
        cours.setDescription(dto.getDescription());
        if (dto.getCoefficient() != null) cours.setCoefficient(dto.getCoefficient());
        if (dto.getVolumeHoraire() != null) cours.setVolumeHoraire(dto.getVolumeHoraire());
        cours.setMatiere(matiere);

        if (dto.getClasseId() != null) {
            Classe classe = classeRepository.findById(dto.getClasseId())
                    .orElseThrow(() -> new RuntimeException("Classe not found with id: " + dto.getClasseId()));
            cours.setClasse(classe);
        }

        // (Optionnel) si tu ajoutes professeurId dans CoursDTO plus tard
        // if (dto.getProfesseurId() != null) {
        //     Professeur prof = professeurRepository.findById(dto.getProfesseurId())
        //             .orElseThrow(() -> new RuntimeException("Professeur not found with id: " + dto.getProfesseurId()));
        //     cours.setProfesseur(prof);
        // }

        return coursRepository.save(cours);
    }

    /* =========================
       Update
       ========================= */

    public Cours updateCours(Long id, CoursDTO dto) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours not found with id: " + id));

        if (dto.getCode() != null) cours.setCode(dto.getCode());
        if (dto.getIntitule() != null) cours.setIntitule(dto.getIntitule());
        if (dto.getDescription() != null) cours.setDescription(dto.getDescription());
        if (dto.getCoefficient() != null) cours.setCoefficient(dto.getCoefficient());
        if (dto.getVolumeHoraire() != null) cours.setVolumeHoraire(dto.getVolumeHoraire());

        if (dto.getMatiereId() != null) {
            Matiere matiere = matiereRepository.findById(dto.getMatiereId())
                    .orElseThrow(() -> new RuntimeException("Matiere not found with id: " + dto.getMatiereId()));
            cours.setMatiere(matiere);
        }

        if (dto.getClasseId() != null) {
            Classe classe = classeRepository.findById(dto.getClasseId())
                    .orElseThrow(() -> new RuntimeException("Classe not found with id: " + dto.getClasseId()));
            cours.setClasse(classe);
        }

        // (Optionnel) si tu ajoutes professeurId dans CoursDTO plus tard
        // if (dto.getProfesseurId() != null) {
        //     Professeur prof = professeurRepository.findById(dto.getProfesseurId())
        //             .orElseThrow(() -> new RuntimeException("Professeur not found with id: " + dto.getProfesseurId()));
        //     cours.setProfesseur(prof);
        // }

        return coursRepository.save(cours);
    }

    /* =========================
       Delete
       ========================= */

    public void deleteCours(Long id) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours not found with id: " + id));
        coursRepository.delete(cours);
    }
}
