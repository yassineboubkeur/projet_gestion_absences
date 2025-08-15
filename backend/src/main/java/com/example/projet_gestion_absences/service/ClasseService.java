package com.example.projet_gestion_absences.service;

//package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.dto.ClasseDTO;
import com.example.projet_gestion_absences.model.dto.ClasseResponseDTO;
import com.example.projet_gestion_absences.model.dto.EtudiantResponseDTO;
import com.example.projet_gestion_absences.model.entity.Classe;
import com.example.projet_gestion_absences.repository.ClasseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClasseService {

    private final ClasseRepository classeRepository;
    private final EtudiantService etudiantService;

    public ClasseService(ClasseRepository classeRepository, EtudiantService etudiantService) {
        this.classeRepository = classeRepository;
        this.etudiantService = etudiantService;
    }

    public ClasseResponseDTO createClasse(ClasseDTO classeDTO) {
        Classe classe = new Classe();
        classe.setNom(classeDTO.getNom());
        classe.setNiveau(classeDTO.getNiveau());

        Classe savedClasse = classeRepository.save(classe);
        return mapToResponseDTO(savedClasse);
    }

    public List<ClasseResponseDTO> getAllClasses() {
        return classeRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ClasseResponseDTO> getClasseById(Long id) {
        return classeRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

    public Optional<ClasseResponseDTO> updateClasse(Long id, ClasseDTO classeDTO) {
        return classeRepository.findById(id)
                .map(classe -> {
                    classe.setNom(classeDTO.getNom());
                    classe.setNiveau(classeDTO.getNiveau());
                    Classe updated = classeRepository.save(classe);
                    return mapToResponseDTO(updated);
                });
    }

    public void deleteClasse(Long id) {
        classeRepository.deleteById(id);
    }

    public List<EtudiantResponseDTO> getEtudiantsByClasse(Long classeId) {
        return etudiantService.getEtudiantsByClasse(classeId);
    }

    public List<ClasseResponseDTO> getClassesByNiveau(String niveau) {
        return classeRepository.findByNiveau(niveau).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private ClasseResponseDTO mapToResponseDTO(Classe classe) {
        ClasseResponseDTO dto = new ClasseResponseDTO();
        dto.setId(classe.getId());
        dto.setNom(classe.getNom());
        dto.setNiveau(classe.getNiveau());
        dto.setEffectif(classe.getEtudiants() != null ? classe.getEtudiants().size() : 0);
        return dto;
    }
}