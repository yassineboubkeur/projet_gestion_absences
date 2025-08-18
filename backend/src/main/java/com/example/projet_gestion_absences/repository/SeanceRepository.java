// src/main/java/com/example/projet_gestion_absences/repository/SeanceRepository.java
package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.Professeur;
import com.example.projet_gestion_absences.model.entity.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findByDate(LocalDate date);
    List<Seance> findByProfesseurId(Long professeurId);
    List<Seance> findByCoursId(Long coursId);
    List<Seance> findBySalleId(Long salleId);

    List<Seance> findByDateAndSalleId(LocalDate date, Long salleId);

    List<Seance> findByProfesseurAndDateAndHeureDebutLessThanAndHeureFinGreaterThan(
            Professeur professeur, LocalDate date, LocalTime endTime, LocalTime startTime);

    @Query("""
           SELECT COUNT(s) > 0
           FROM Seance s
           WHERE s.cours.classe.id = :classeId
             AND s.date = :date
             AND s.heureDebut < :endTime
             AND s.heureFin   > :startTime
           """)
    boolean classeHasOverlap(@Param("classeId") Long classeId,
                             @Param("date") LocalDate date,
                             @Param("startTime") LocalTime startTime,
                             @Param("endTime") LocalTime endTime);
}
