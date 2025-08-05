package com.example.projet_gestion_absences.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Etudiant extends Utilisateur {
    private String matricule;

    @ManyToOne
    private Classe classe;

    // Constructors, getters, and setters
    public Etudiant() {}

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }
}