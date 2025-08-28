package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface SalleRepository extends JpaRepository<Salle,Long> {
    Salle findByCode(String code);

    // Find available classrooms for a specific time slot
    @Query("SELECT s FROM Salle s WHERE s NOT IN " +
            "(SELECT se.salle FROM Seance se WHERE se.date = :date " +
            "AND ((se.heureDebut < :endTime AND se.heureFin > :startTime))) " +
            "ORDER BY s.capacite DESC")
    List<Salle> findAvailableSalles(
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    // Optional: Find by capacity greater than or equal to required
    List<Salle> findByCapaciteGreaterThanEqual(int capacite);

    List<Salle> findByIdNotIn(Collection<Long> ids);
    List<com.example.projet_gestion_absences.model.entity.Salle> findByIdNotIn(List<Long> ids);

}
