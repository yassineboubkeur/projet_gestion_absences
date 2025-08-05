package com.example.projet_gestion_absences.model.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Matiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String intitule;
    private String domaine;

    @OneToMany(mappedBy = "matiere")
    private List<Cours> cours;

    // Constructors, Getters and Setters
    public Matiere() {}

    public Matiere(String code, String intitule) {
        this.code = code;
        this.intitule = intitule;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }
    public String getDomaine() { return domaine; }
    public void setDomaine(String domaine) { this.domaine = domaine; }
    public List<Cours> getCours() { return cours; }
    public void setCours(List<Cours> cours) { this.cours = cours; }

    @Override
    public String toString() {
        return code + " - " + intitule;
    }
}