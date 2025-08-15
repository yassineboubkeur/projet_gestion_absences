package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByLogin(String login);

    // Add this method to find students by class ID
    List<Etudiant> findByClasseId(Long classeId);

    // Optional: Add this if you need case-insensitive search by matricule
    Optional<Etudiant> findByMatriculeIgnoreCase(String matricule);
}
