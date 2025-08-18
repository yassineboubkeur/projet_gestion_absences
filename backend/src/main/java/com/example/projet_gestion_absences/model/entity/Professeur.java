package com.example.projet_gestion_absences.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Professeur extends Utilisateur {
    private String matricule;
    private String specialite;
    private String adresse;
    private LocalDate dateNaissance;

    @OneToMany(mappedBy = "professeur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("prof-seances")
    private List<Seance> seances = new java.util.ArrayList<>();

    // Constructors
    public Professeur() {
        // Default constructor required by JPA
    }

    public Professeur(String matricule,String specialite, String adresse, LocalDate dateNaissance) {
        this.matricule = matricule;
        this.specialite = specialite;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
    }

    // Getters and Setters
    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getSpecialite() {
        return specialite;
    }
    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getAdresse() {
        return adresse;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
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