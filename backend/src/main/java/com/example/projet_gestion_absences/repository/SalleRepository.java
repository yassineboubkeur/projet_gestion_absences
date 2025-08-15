package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalleRepository extends JpaRepository<Salle,Long> {
    Salle findByCode(String code);
}
