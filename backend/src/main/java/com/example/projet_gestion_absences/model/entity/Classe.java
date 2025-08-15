package com.example.projet_gestion_absences.model.entity;

//package com.example.projet_gestion_absences.model.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;  // e.g., "Terminale A"
    private String niveau; // e.g., "Terminale", "3ème"

    @OneToMany(mappedBy = "classe")
    @JsonIgnore

    private List<Etudiant> etudiants;

    // Constructors, getters, and setters
    public Classe() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }
}
