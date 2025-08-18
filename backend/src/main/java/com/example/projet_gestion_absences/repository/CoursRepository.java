package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.Classe;
import com.example.projet_gestion_absences.model.entity.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {
    List<Cours> findByMatiereId(Long matiereId);
    List<Cours> findByIntituleContainingIgnoreCase(String intitule);
    List<Cours> findByCode(String code);

    List<Cours> findByClasse(Classe classe);

    @Query("SELECT c FROM Cours c WHERE c.classe.id = :classeId")
    List<Cours> findByClasseId(@Param("classeId") Long classeId);
}