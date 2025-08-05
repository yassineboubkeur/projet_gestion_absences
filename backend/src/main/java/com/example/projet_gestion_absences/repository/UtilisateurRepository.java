package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByLogin(String login);
    Boolean existsByLogin(String login);
    Boolean existsByEmail(String email);
}
