package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.Classe;
import com.example.projet_gestion_absences.model.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    // Basic CRUD operations are inherited from JpaRepository

    // Find class by name
    Optional<Classe> findByNom(String nom);

    // Find classes by level (niveau)
    List<Classe> findByNiveau(String niveau);

    // Find all classes ordered by name
    List<Classe> findAllByOrderByNomAsc();

    // Find students in a specific class
    @Query("SELECT e FROM Etudiant e WHERE e.classe.id = :classeId")
    List<Etudiant> findEtudiantsByClasseId(Long classeId);

    // Count students in a class
    @Query("SELECT COUNT(e) FROM Etudiant e WHERE e.classe.id = :classeId")
    Long countEtudiantsByClasseId(Long classeId);

    // Find classes with student count
    @Query("SELECT c, COUNT(e) FROM Classe c LEFT JOIN c.etudiants e GROUP BY c")
    List<Object[]> findAllWithStudentCount();

    // Find classes by name containing (case insensitive)
    List<Classe> findByNomContainingIgnoreCase(String nom);

    // Find active classes
    @Query("SELECT c FROM Classe c WHERE SIZE(c.etudiants) > 0")
    List<Classe> findActiveClasses();
}