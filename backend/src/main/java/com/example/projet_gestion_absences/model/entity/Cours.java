package com.example.projet_gestion_absences.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String intitule;
    private String description;
    private int coefficient;
    private int volumeHoraire;

    @ManyToOne
    @JoinColumn(name = "matiere_id")
    private Matiere matiere;

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    @ManyToOne
    @JoinColumn(name = "professeur_id")
    private Professeur professeur;

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("cours-seances")
    private List<Seance> seances = new java.util.ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;
    // Constructors
    public Cours() {}

    public Cours(String code, String intitule, Matiere matiere, Professeur professeur) {
        this.code = code;
        this.intitule = intitule;
        this.matiere = matiere;
        this.professeur = professeur;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getCoefficient() { return coefficient; }
    public void setCoefficient(int coefficient) { this.coefficient = coefficient; }
    public int getVolumeHoraire() { return volumeHoraire; }
    public void setVolumeHoraire(int volumeHoraire) { this.volumeHoraire = volumeHoraire; }
    public Matiere getMatiere() { return matiere; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }
    public Professeur getProfesseur() { return professeur; }
    public void setProfesseur(Professeur professeur) { this.professeur = professeur; }
    public List<Seance> getSeances() { return seances; }
    public void setSeances(List<Seance> seances) { this.seances = seances; }

    @Override
    public String toString() {
        return code + " - " + intitule;
    }
}