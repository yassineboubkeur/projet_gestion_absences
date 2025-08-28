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

    // ------- Existant -------
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

    // ------- NOUVEAU : Conflit pour prof OU salle (un seul appel) -------
    @Query("""
           SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
           FROM Seance s
           WHERE s.date = :date
             AND (
                   (:salleId IS NOT NULL AND s.salle.id = :salleId)
                OR (:profId  IS NOT NULL AND s.professeur.id = :profId)
             )
             AND s.heureDebut < :end
             AND s.heureFin   > :start
           """)
    boolean existsConflict(@Param("date") LocalDate date,
                           @Param("start") LocalTime start,
                           @Param("end") LocalTime end,
                           @Param("salleId") Long salleId,
                           @Param("profId") Long profId);

    // Variante utile si tu veux ignorer une séance (pour extend sur elle-même)
    @Query("""
           SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
           FROM Seance s
           WHERE s.date = :date
             AND s.id <> :excludeId
             AND (
                   (:salleId IS NOT NULL AND s.salle.id = :salleId)
                OR (:profId  IS NOT NULL AND s.professeur.id = :profId)
             )
             AND s.heureDebut < :end
             AND s.heureFin   > :start
           """)
    boolean existsConflictExcludingId(@Param("date") LocalDate date,
                                      @Param("start") LocalTime start,
                                      @Param("end") LocalTime end,
                                      @Param("salleId") Long salleId,
                                      @Param("profId") Long profId,
                                      @Param("excludeId") Long excludeId);

    // ------- NOUVEAU : IDs occupés (pour endpoints /available) -------
    @Query("""
           SELECT DISTINCT s.professeur.id
           FROM Seance s
           WHERE s.professeur IS NOT NULL
             AND s.date = :date
             AND s.heureDebut < :end
             AND s.heureFin   > :start
           """)
    List<Long> findBusyProfIds(@Param("date") LocalDate date,
                               @Param("start") LocalTime start,
                               @Param("end") LocalTime end);

    @Query("""
           SELECT DISTINCT s.salle.id
           FROM Seance s
           WHERE s.salle IS NOT NULL
             AND s.date = :date
             AND s.heureDebut < :end
             AND s.heureFin   > :start
           """)
    List<Long> findBusySalleIds(@Param("date") LocalDate date,
                                @Param("start") LocalTime start,
                                @Param("end") LocalTime end);
}
