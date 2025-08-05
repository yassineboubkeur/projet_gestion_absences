package com.example.projet_gestion_absences.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Professeur extends Utilisateur {
    private String matricule;

    @OneToMany(mappedBy = "professeur")
    private List<Seance> seances;

    // Constructors
    public Professeur() {
        // Default constructor required by JPA
    }

    public Professeur(String matricule) {
        this.matricule = matricule;
    }

    // Getters and Setters
    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public List<Seance> getSeances() {
        return seances;
    }

    public void setSeances(List<Seance> seances) {
        this.seances = seances;
    }

    // Additional methods if needed
    @Override
    public String toString() {
        return "Professeur{" +
                "matricule='" + matricule + '\'' +
                ", nom='" + getNom() + '\'' +
                ", prenom='" + getPrenom() + '\'' +
                '}';
    }
}