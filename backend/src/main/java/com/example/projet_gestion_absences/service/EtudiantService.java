package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.entity.Etudiant;
import com.example.projet_gestion_absences.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.projet_gestion_absences.model.enums.Role;

import java.util.List;
import java.util.Optional;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    public List<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }

    public Optional<Etudiant> findById(Long id) {
        return etudiantRepository.findById(id);
    }

    public Optional<Etudiant> findByLogin(String login) {
        return etudiantRepository.findByLogin(login);
    }

    public Etudiant save(Etudiant etudiant) {
        // S’assurer que le rôle est bien ROLE_ETUDIANT
        etudiant.setRole(Role.ROLE_ETUDIANT);
        return etudiantRepository.save(etudiant);
    }

    public void deleteById(Long id) {
        etudiantRepository.deleteById(id);
    }
}
