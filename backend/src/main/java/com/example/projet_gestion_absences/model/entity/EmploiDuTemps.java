package com.example.projet_gestion_absences.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class EmploiDuTemps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String intitule;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private boolean actif;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;

    @OneToMany(mappedBy = "emploiDuTemps", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seance> seances;

    // Constructors
    public EmploiDuTemps() {}

    public EmploiDuTemps(String intitule, Classe classe, LocalDate dateDebut, LocalDate dateFin) {
        this.intitule = intitule;
        this.classe = classe;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.actif = true;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    public Classe getClasse() { return classe; }
    public void setClasse(Classe classe) { this.classe = classe; }
    public List<Seance> getSeances() { return seances; }
    public void setSeances(List<Seance> seances) { this.seances = seances; }

    public void ajouterSeance(Seance seance) {
        seances.add(seance);
        seance.setEmploiDuTemps(this);
    }

    public void supprimerSeance(Seance seance) {
        seances.remove(seance);
        seance.setEmploiDuTemps(null);
    }
}