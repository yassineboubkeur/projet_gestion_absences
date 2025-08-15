package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.entity.Matiere;
import com.example.projet_gestion_absences.repository.MatiereRepository;
import com.example.projet_gestion_absences.model.dto.MatiereDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MatiereService {

    private final MatiereRepository matiereRepository;

    public MatiereService(MatiereRepository matiereRepository) {
        this.matiereRepository = matiereRepository;
    }

    // Create
    public Matiere createMatiere(MatiereDTO matiereDTO) {
        Matiere matiere = new Matiere();
        matiere.setCode(matiereDTO.getCode());
        matiere.setIntitule(matiereDTO.getIntitule());
        matiere.setDomaine(matiereDTO.getDomaine());
        return matiereRepository.save(matiere);
    }

    // Read
    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }

    public Optional<Matiere> getMatiereById(Long id) {
        return matiereRepository.findById(id);
    }

    public List<Matiere> getMatieresByDomaine(String domaine) {
        return matiereRepository.findByDomaine(domaine);
    }

    public List<Matiere> searchByIntitule(String intitule) {
        return matiereRepository.findByIntituleContainingIgnoreCase(intitule);
    }

    public Matiere getMatiereByCode(String code) {
        return matiereRepository.findByCode(code);
    }

    // Update
    public Matiere updateMatiere(Long id, MatiereDTO matiereDTO) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matiere not found with id: " + id));

        if (matiereDTO.getCode() != null) {
            matiere.setCode(matiereDTO.getCode());
        }
        if (matiereDTO.getIntitule() != null) {
            matiere.setIntitule(matiereDTO.getIntitule());
        }
        if (matiereDTO.getDomaine() != null) {
            matiere.setDomaine(matiereDTO.getDomaine());
        }

        return matiereRepository.save(matiere);
    }

    // Delete
    public void deleteMatiere(Long id) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matiere not found with id: " + id));
        matiereRepository.delete(matiere);
    }
}