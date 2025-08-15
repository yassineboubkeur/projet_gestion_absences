package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {
    List<Cours> findByMatiereId(Long matiereId);
    List<Cours> findByIntituleContainingIgnoreCase(String intitule);
    List<Cours> findByCode(String code);
}