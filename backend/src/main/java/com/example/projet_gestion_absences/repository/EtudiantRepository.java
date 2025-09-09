package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.Etudiant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    // Hérité de Utilisateur : le login est sur la super-classe
    Optional<Etudiant> findByLogin(String login);

    List<Etudiant> findByClasseId(Long classeId);

    Optional<Etudiant> findByMatriculeIgnoreCase(String matricule);

    // Si tu veux toujours un chargement eager de la classe pour un findById :
    @Override
    @EntityGraph(attributePaths = {"classe"})
    Optional<Etudiant> findById(Long id);

    // IMPORTANT : En héritage, l'id Etudiant == id Utilisateur
    // Donc on ne fait PAS e.utilisateur.id (champ inexistant), mais e.id
    @Query("select e.id from Etudiant e where e.id = :userId")
    Optional<Long> findIdByUtilisateurId(@Param("userId") Long userId);
}
