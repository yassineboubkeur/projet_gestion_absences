package com.example.projet_gestion_absences.repository;

// src/main/java/com/example/projet_gestion_absences/repository/EmploiDuTempsRepository.java
//package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.EmploiDuTemps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmploiDuTempsRepository extends JpaRepository<EmploiDuTemps, Long> {
    List<EmploiDuTemps> findByClasseId(Long classeId);
    Optional<EmploiDuTemps> findTopByClasseIdOrderByDateDebutDesc(Long classeId);
}
