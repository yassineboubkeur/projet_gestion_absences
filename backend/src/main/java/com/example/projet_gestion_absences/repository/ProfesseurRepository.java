package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {

    // Basic search methods
    List<Professeur> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    Optional<Professeur> findByMatricule(String matricule);

    // Specialty-related methods
    @Query("SELECT DISTINCT p.specialite FROM Professeur p WHERE p.specialite IS NOT NULL ORDER BY p.specialite ASC")
    List<String> findDistinctSpecialites();

    List<Professeur> findBySpecialiteIgnoreCase(String specialite);

    // Additional useful queries (examples)
    @Query("SELECT p FROM Professeur p WHERE UPPER(p.specialite) LIKE UPPER(CONCAT('%', :keyword, '%'))")
    List<Professeur> findBySpecialiteContainingIgnoreCase(@Param("keyword") String keyword);

    List<Professeur> findByDateNaissanceBetween(LocalDate startDate, LocalDate endDate);
    List<Professeur> findByIdNotIn(Collection<Long> ids);
    List<Professeur> findByIdNotIn(List<Long> ids);




}