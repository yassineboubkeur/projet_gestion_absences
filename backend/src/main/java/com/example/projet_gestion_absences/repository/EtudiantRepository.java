package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    Optional<Etudiant> findByLogin(String login);

    List<Etudiant> findByClasseId(Long classeId);

    Optional<Etudiant> findByMatriculeIgnoreCase(String matricule);

    // ⚠️ Ne pas laisser ceci si ton entité Etudiant n'a PAS d'association 'utilisateur'
    // @Query("select e from Etudiant e where e.utilisateur.id = :utilisateurId")
    // Optional<Etudiant> findByUtilisateurId(@Param("utilisateurId") Long utilisateurId);

}


//package com.example.projet_gestion_absences.repository;
//
//import com.example.projet_gestion_absences.model.entity.Etudiant;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
//
//    // Déjà présent : recherche par login (si tu fais correspondre JWT.sub -> etudiant.login)
//    Optional<Etudiant> findByLogin(String login);
//
//    // Déjà présent : liste des étudiants d'une classe
//    List<Etudiant> findByClasseId(Long classeId);
//
//    // Déjà présent : recherche par matricule (case-insensitive)
//    Optional<Etudiant> findByMatriculeIgnoreCase(String matricule);
//
//    // ★ Nouveau : utilisé pour sécuriser /api/emploi-du-temps/me/pdf et /etudiant/{id}/pdf
//    // Suppose que Etudiant possède un champ 'utilisateur' (OneToOne/ManyToOne) avec un 'id'
//    Optional<Etudiant> findByUtilisateurId(Long utilisateurId);
//
//    // (Optionnel) Si tu préfères mapper via le login du compte utilisateur :
//    // Optional<Etudiant> findByUtilisateur_Login(String login);
//
//
//}
