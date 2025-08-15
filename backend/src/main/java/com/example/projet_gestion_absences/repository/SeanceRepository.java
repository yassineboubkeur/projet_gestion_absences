package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findByDate(LocalDate date);
    List<Seance> findByProfesseurId(Long professeurId);
    List<Seance> findByCoursId(Long coursId);
    List<Seance> findBySalleId(Long salleId);

    List<Seance> findByDateAndSalleId(LocalDate date, Long salleId);
}
