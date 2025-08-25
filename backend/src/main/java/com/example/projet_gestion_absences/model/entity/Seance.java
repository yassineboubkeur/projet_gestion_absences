package com.example.projet_gestion_absences.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "seance")
public class Seance {

    public enum StatutSeance {
        PLANIFIEE,
        EFFECTUEE,
        ANNULEE,
        REPORTEE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime heureDebut;

    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    private StatutSeance statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cours_id")
    private Cours cours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professeur_id")
    private Professeur professeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salle_id")
    private Salle salle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emploi_du_temps_id")
    private EmploiDuTemps emploiDuTemps;

    @Column(length = 32)
    private String color; // ex: "#A3E635" ou "emerald-300"

    /* --------- Getters / Setters --------- */

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }

    public StatutSeance getStatut() { return statut; }
    public void setStatut(StatutSeance statut) { this.statut = statut; }

    public Cours getCours() { return cours; }
    public void setCours(Cours cours) { this.cours = cours; }

    public Professeur getProfesseur() { return professeur; }
    public void setProfesseur(Professeur professeur) { this.professeur = professeur; }

    public Salle getSalle() { return salle; }
    public void setSalle(Salle salle) { this.salle = salle; }

    public EmploiDuTemps getEmploiDuTemps() { return emploiDuTemps; }
    public void setEmploiDuTemps(EmploiDuTemps emploiDuTemps) { this.emploiDuTemps = emploiDuTemps; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}



//package com.example.projet_gestion_absences.model.entity;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "seance")
//public class Seance {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private LocalDate date;
//    private LocalTime heureDebut;
//    private LocalTime heureFin;
//
//    public enum StatutSeance { PLANIFIEE, EFFECTUEE, ANNULEE, REPORTEE }
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "statut", nullable = false)
//    private StatutSeance statut = StatutSeance.PLANIFIEE;
//
//    // —— Relations ——
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cours_id")
//    @JsonBackReference("cours-seances")        // <— empêche cours → seances → cours…
//    private Cours cours;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "professeur_id")
//    @JsonBackReference("prof-seances")         // <— empêche prof → seances → prof…
//    private Professeur professeur;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "salle_id")
//    @JsonBackReference(value = "salle-seances") // <— si Salle possède une liste de seances
//    private Salle salle;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "edt_id")
//    @JsonBackReference("edt-seances")          // <— empêche edt → seances → edt…
//    private EmploiDuTemps emploiDuTemps;
//
//    @OneToMany(mappedBy = "seance", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Absence> absences = new ArrayList<>();
//
//    public Seance() {}
//
//    // Getters / Setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public LocalDate getDate() { return date; }
//    public void setDate(LocalDate date) { this.date = date; }
//
//    public LocalTime getHeureDebut() { return heureDebut; }
//    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }
//
//    public LocalTime getHeureFin() { return heureFin; }
//    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }
//
//    public StatutSeance getStatut() { return statut; }
//    public void setStatut(StatutSeance statut) { this.statut = statut; }
//
//    public Cours getCours() { return cours; }
//    public void setCours(Cours cours) { this.cours = cours; }
//
//    public Professeur getProfesseur() { return professeur; }
//    public void setProfesseur(Professeur professeur) { this.professeur = professeur; }
//
//    public Salle getSalle() { return salle; }
//    public void setSalle(Salle salle) { this.salle = salle; }
//
//    public EmploiDuTemps getEmploiDuTemps() { return emploiDuTemps; }
//    public void setEmploiDuTemps(EmploiDuTemps emploiDuTemps) { this.emploiDuTemps = emploiDuTemps; }
//
//    public List<Absence> getAbsences() { return absences; }
//    public void setAbsences(List<Absence> absences) { this.absences = absences; }
//}
