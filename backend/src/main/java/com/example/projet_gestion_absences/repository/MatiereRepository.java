package com.example.projet_gestion_absences.repository;


import com.example.projet_gestion_absences.model.entity.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {
    List<Matiere> findByDomaine(String domaine);
    List<Matiere> findByIntituleContainingIgnoreCase(String intitule);
    Matiere findByCode(String code);
}
