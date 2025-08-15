package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.dto.EtudiantDTO;
import com.example.projet_gestion_absences.model.dto.EtudiantResponseDTO;
import com.example.projet_gestion_absences.model.entity.Classe;
import com.example.projet_gestion_absences.model.entity.Etudiant;
import com.example.projet_gestion_absences.model.enums.Role;
import com.example.projet_gestion_absences.repository.ClasseRepository;
import com.example.projet_gestion_absences.repository.EtudiantRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final ClasseRepository classeRepository;
    private final PasswordEncoder passwordEncoder;

    public EtudiantService(EtudiantRepository etudiantRepository,
                           ClasseRepository classeRepository,
                           PasswordEncoder passwordEncoder) {
        this.etudiantRepository = etudiantRepository;
        this.classeRepository = classeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Student CRUD methods
    public EtudiantResponseDTO createEtudiant(EtudiantDTO etudiantDTO) {
        Classe classe = classeRepository.findById(etudiantDTO.getClasseId())
                .orElseThrow(() -> new RuntimeException("Classe not found"));

        Etudiant etudiant = new Etudiant();
        // Map fields from DTO to entity
        etudiant.setNom(etudiantDTO.getNom());
        etudiant.setPrenom(etudiantDTO.getPrenom());
        etudiant.setEmail(etudiantDTO.getEmail());
        etudiant.setLogin(etudiantDTO.getLogin());
        etudiant.setPassword(passwordEncoder.encode(etudiantDTO.getPassword()));
        etudiant.setMatricule(etudiantDTO.getMatricule());
        etudiant.setAddress(etudiantDTO.getAddress());
        etudiant.setClasse(classe);
        etudiant.setRole(Role.ROLE_ETUDIANT);

        Etudiant savedEtudiant = etudiantRepository.save(etudiant);
        return mapToResponseDTO(savedEtudiant);
    }

    public List<EtudiantResponseDTO> getAllEtudiants() {
        return etudiantRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<EtudiantResponseDTO> getEtudiantById(Long id) {
        return etudiantRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

    public Optional<EtudiantResponseDTO> updateEtudiant(Long id, EtudiantDTO etudiantDTO) {
        return etudiantRepository.findById(id)
                .map(etudiant -> {
                    // Update fields
                    etudiant.setNom(etudiantDTO.getNom());
                    etudiant.setPrenom(etudiantDTO.getPrenom());
                    etudiant.setEmail(etudiantDTO.getEmail());
                    etudiant.setLogin(etudiantDTO.getLogin());
                    if (etudiantDTO.getPassword() != null) {
                        etudiant.setPassword(passwordEncoder.encode(etudiantDTO.getPassword()));
                    }
                    etudiant.setMatricule(etudiantDTO.getMatricule());
                    etudiant.setAddress(etudiantDTO.getAddress());

                    // Update class if changed
                    if (!etudiant.getClasse().getId().equals(etudiantDTO.getClasseId())) {
                        Classe newClasse = classeRepository.findById(etudiantDTO.getClasseId())
                                .orElseThrow(() -> new RuntimeException("Classe not found"));
                        etudiant.setClasse(newClasse);
                    }

                    Etudiant updated = etudiantRepository.save(etudiant);
                    return mapToResponseDTO(updated);
                });
    }

    public void deleteEtudiant(Long id) {
        etudiantRepository.deleteById(id);
    }

    // Class-related methods
    public List<EtudiantResponseDTO> getEtudiantsByClasse(Long classeId) {
        return etudiantRepository.findByClasseId(classeId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<EtudiantResponseDTO> changeClasse(Long etudiantId, Long newClasseId) {
        return etudiantRepository.findById(etudiantId)
                .map(etudiant -> {
                    Classe newClasse = classeRepository.findById(newClasseId)
                            .orElseThrow(() -> new RuntimeException("Classe not found"));
                    etudiant.setClasse(newClasse);
                    Etudiant updated = etudiantRepository.save(etudiant);
                    return mapToResponseDTO(updated);
                });
    }

    // Helper method
    private EtudiantResponseDTO mapToResponseDTO(Etudiant etudiant) {
        EtudiantResponseDTO dto = new EtudiantResponseDTO();
        dto.setId(etudiant.getId());
        dto.setNom(etudiant.getNom());
        dto.setPrenom(etudiant.getPrenom());
        dto.setEmail(etudiant.getEmail());
        dto.setMatricule(etudiant.getMatricule());
        dto.setAddress(etudiant.getAddress());
        dto.setClasse(etudiant.getClasse());
        return dto;
    }
}

//package com.example.projet_gestion_absences.service;
//
//import com.example.projet_gestion_absences.model.entity.Etudiant;
//import com.example.projet_gestion_absences.repository.EtudiantRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.example.projet_gestion_absences.model.enums.Role;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class EtudiantService {
//
//    @Autowired
//    private EtudiantRepository etudiantRepository;
//
//    public List<Etudiant> findAll() {
//        return etudiantRepository.findAll();
//    }
//
//    public Optional<Etudiant> findById(Long id) {
//        return etudiantRepository.findById(id);
//    }
//
//    public Optional<Etudiant> findByLogin(String login) {
//        return etudiantRepository.findByLogin(login);
//    }
//
//    public Etudiant save(Etudiant etudiant) {
//        // S’assurer que le rôle est bien ROLE_ETUDIANT
//        etudiant.setRole(Role.ROLE_ETUDIANT);
//        return etudiantRepository.save(etudiant);
//    }
//
//    public void deleteById(Long id) {
//        etudiantRepository.deleteById(id);
//    }
//}
