package com.example.projet_gestion_absences.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Etudiant extends Utilisateur {


    private String matricule;
    private String address; // Nouveau champ adresse

    @ManyToOne
    @JsonIgnore
    private Classe classe;

    // Constructors
    public Etudiant() {}

    // Getters et setters
    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }
}
