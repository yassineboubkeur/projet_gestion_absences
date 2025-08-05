package com.example.projet_gestion_absences.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Seance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;

    @ManyToOne
    @JoinColumn(name = "professeur_id", nullable = false)
    private Professeur professeur;

    @ManyToOne
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;

    @ManyToOne
    @JoinColumn(name = "emploi_du_temps_id")
    private EmploiDuTemps emploiDuTemps;

    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    private StatutSeance statut = StatutSeance.PLANIFIEE;

    @OneToMany(mappedBy = "seance", cascade = CascadeType.ALL)
    private List<Absence> absences;

    // Constructors
    public Seance() {
        // Default constructor required by JPA
    }

    public Seance(Cours cours, Professeur professeur, Salle salle, LocalDate date,
                  LocalTime heureDebut, LocalTime heureFin) {
        this.cours = cours;
        this.professeur = professeur;
        this.salle = salle;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Cours getCours() {
        return cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public Professeur getProfesseur() {
        return professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public EmploiDuTemps getEmploiDuTemps() {
        return emploiDuTemps;
    }

    public void setEmploiDuTemps(EmploiDuTemps emploiDuTemps) {
        this.emploiDuTemps = emploiDuTemps;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public StatutSeance getStatut() {
        return statut;
    }

    public void setStatut(StatutSeance statut) {
        this.statut = statut;
    }

    public List<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(List<Absence> absences) {
        this.absences = absences;
    }

    // Enum for session status
    public enum StatutSeance {
        PLANIFIEE,
        EFFECTUEE,
        ANNULEE,
        REPORTEE
    }

    // Utility methods
    public boolean estEnConflitHoraires(Seance autreSeance) {
        return this.date.equals(autreSeance.getDate()) &&
                this.heureDebut.isBefore(autreSeance.getHeureFin()) &&
                this.heureFin.isAfter(autreSeance.getHeureDebut());
    }

    @Override
    public String toString() {
        return "Seance{" +
                "id=" + id +
                ", cours=" + cours.getIntitule() +
                ", date=" + date +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", salle=" + salle.getCode() +
                '}';
    }
}