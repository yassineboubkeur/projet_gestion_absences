package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {

    // Liste des absences d’une séance
    List<Absence> findBySeanceId(Long seanceId);

    // Compteurs globaux
    long countBySeanceId(Long seanceId);
    long countBySeanceIdAndJustifieeTrue(Long seanceId);
    long countBySeanceIdAndJustifieeFalse(Long seanceId);

    // Recherche / existence par (séance, étudiant)
    Optional<Absence> findBySeanceIdAndEtudiantId(Long seanceId, Long etudiantId);
    boolean existsBySeanceIdAndEtudiantId(Long seanceId, Long etudiantId);
}
