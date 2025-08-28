package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.dto.ProfesseurDTO;
import com.example.projet_gestion_absences.model.dto.ProfesseurResponseDTO;
import com.example.projet_gestion_absences.model.dto.SpecialiteWithProfesseursDTO;
import com.example.projet_gestion_absences.model.entity.Professeur;
import com.example.projet_gestion_absences.model.enums.Role;
import com.example.projet_gestion_absences.repository.ProfesseurRepository;
import com.example.projet_gestion_absences.repository.SeanceRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfesseurService {

    private final ProfesseurRepository professeurRepository;
    private final SeanceRepository seanceRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfesseurService(ProfesseurRepository professeurRepository,
                             SeanceRepository seanceRepository,
                             PasswordEncoder passwordEncoder) {
        this.professeurRepository = professeurRepository;
        this.seanceRepository = seanceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<String> getAllSpecialites() {
        return professeurRepository.findDistinctSpecialites();
    }

    // Create
    public ProfesseurResponseDTO createProfesseur(ProfesseurDTO professeurDTO) {
        Professeur professeur = new Professeur();
        professeur.setNom(professeurDTO.getNom());
        professeur.setPrenom(professeurDTO.getPrenom());
        professeur.setEmail(professeurDTO.getEmail());
        professeur.setLogin(professeurDTO.getLogin());
        professeur.setPassword(passwordEncoder.encode(professeurDTO.getPassword()));
        professeur.setMatricule(professeurDTO.getMatricule());
        professeur.setSpecialite(professeurDTO.getSpecialite());
        professeur.setAdresse(professeurDTO.getAdresse());
        professeur.setDateNaissance(professeurDTO.getDateNaissance());
        professeur.setRole(Role.ROLE_PROFESSEUR);
        professeur.setActive(true);

        Professeur savedProfesseur = professeurRepository.save(professeur);
        return mapToResponseDTO(savedProfesseur);
    }

    // Read
    @Transactional(readOnly = true)
    public List<ProfesseurResponseDTO> getAllProfesseurs() {
        return professeurRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProfesseurResponseDTO> getProfesseurById(Long id) {
        return professeurRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<ProfesseurResponseDTO> searchProfesseurs(String searchTerm) {
        return professeurRepository
                .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(searchTerm, searchTerm)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProfesseurResponseDTO> getProfesseurByMatricule(String matricule) {
        return professeurRepository.findByMatricule(matricule)
                .map(this::mapToResponseDTO);
    }

    // Update
    public Optional<ProfesseurResponseDTO> updateProfesseur(Long id, ProfesseurDTO professeurDTO) {
        return professeurRepository.findById(id)
                .map(professeur -> {
                    professeur.setNom(professeurDTO.getNom());
                    professeur.setPrenom(professeurDTO.getPrenom());
                    professeur.setEmail(professeurDTO.getEmail());
                    professeur.setLogin(professeurDTO.getLogin());
                    if (professeurDTO.getPassword() != null && !professeurDTO.getPassword().isEmpty()) {
                        professeur.setPassword(passwordEncoder.encode(professeurDTO.getPassword()));
                    }
                    professeur.setMatricule(professeurDTO.getMatricule());
                    professeur.setSpecialite(professeurDTO.getSpecialite());
                    professeur.setAdresse(professeurDTO.getAdresse());
                    professeur.setDateNaissance(professeurDTO.getDateNaissance());
                    Professeur updatedProfesseur = professeurRepository.save(professeur);
                    return mapToResponseDTO(updatedProfesseur);
                });
    }

    @Transactional(readOnly = true)
    public List<ProfesseurResponseDTO> getProfesseursBySpecialite(String specialite) {
        return professeurRepository.findBySpecialiteIgnoreCase(specialite).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SpecialiteWithProfesseursDTO> getProfesseursGroupedBySpecialite() {
        List<String> specialites = professeurRepository.findDistinctSpecialites();
        return specialites.stream()
                .map(specialite -> {
                    SpecialiteWithProfesseursDTO dto = new SpecialiteWithProfesseursDTO();
                    dto.setSpecialite(specialite);
                    dto.setProfesseurs(professeurRepository.findBySpecialiteIgnoreCase(specialite)
                            .stream()
                            .map(this::mapToResponseDTO)
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Toggle active status
    public Optional<ProfesseurResponseDTO> toggleProfesseurStatus(Long id) {
        return professeurRepository.findById(id)
                .map(professeur -> {
                    professeur.setActive(!professeur.isActive());
                    Professeur updatedProfesseur = professeurRepository.save(professeur);
                    return mapToResponseDTO(updatedProfesseur);
                });
    }

    // Delete
    public void deleteProfesseur(Long id) {
        professeurRepository.deleteById(id);
    }

    // -------- DISPONIBILITÉ (utilisé par /api/professeurs/available) --------
    @Transactional(readOnly = true)
    public List<ProfesseurResponseDTO> getAvailable(LocalDate date, LocalTime start, LocalTime end) {
        // IDs de profs occupés sur ce créneau (requête dans SeanceRepository)
        List<Long> busy = seanceRepository.findBusyProfIds(date, start, end);

        // Si aucun n'est occupé → tout le monde est dispo
        List<Professeur> free = (busy == null || busy.isEmpty())
                ? professeurRepository.findAll()
                : professeurRepository.findByIdNotIn(busy);

        // Ne renvoyer que les profs actifs
        return free.stream()
                .filter(Professeur::isActive)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Mapper method
    private ProfesseurResponseDTO mapToResponseDTO(Professeur professeur) {
        ProfesseurResponseDTO dto = new ProfesseurResponseDTO();
        dto.setId(professeur.getId());
        dto.setNom(professeur.getNom());
        dto.setPrenom(professeur.getPrenom());
        dto.setEmail(professeur.getEmail());
        dto.setMatricule(professeur.getMatricule());
        dto.setSpecialite(professeur.getSpecialite());
        dto.setAdresse(professeur.getAdresse());
        dto.setDateNaissance(professeur.getDateNaissance());
        dto.setActive(professeur.isActive());
        return dto;
    }
}
