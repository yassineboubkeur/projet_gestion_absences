package com.example.projet_gestion_absences.model.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Salle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String batiment;
    private int capacite;
    private String type; // "Amphithéâtre", "Salle TD", "Laboratoire"

    @OneToMany(mappedBy = "salle")
    private List<Seance> seances;

    // Constructors
    public Salle() {}

    public Salle(String code, String batiment, int capacite, String type) {
        this.code = code;
        this.batiment = batiment;
        this.capacite = capacite;
        this.type = type;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getBatiment() { return batiment; }
    public void setBatiment(String batiment) { this.batiment = batiment; }
    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public List<Seance> getSeances() { return seances; }
    public void setSeances(List<Seance> seances) { this.seances = seances; }

    @Override
    public String toString() {
        return batiment + " - " + code + " (" + type + ")";
    }


}