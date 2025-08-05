package com.example.projet_gestion_absences.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Absence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "seance_id", nullable = false)
    private Seance seance;

    private boolean justifiee;
    private String motif;
    private LocalDateTime dateDeclaration;

    @OneToOne(mappedBy = "absence", cascade = CascadeType.ALL, orphanRemoval = true)
    private Justificatif justificatif;

    // Constructors
    public Absence() {
        this.dateDeclaration = LocalDateTime.now();
    }

    public Absence(Etudiant etudiant, Seance seance) {
        this();
        this.etudiant = etudiant;
        this.seance = seance;
        this.justifiee = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public Etudiant getEtudiant() { return etudiant; }
    public void setEtudiant(Etudiant etudiant) { this.etudiant = etudiant; }
    public Seance getSeance() { return seance; }
    public void setSeance(Seance seance) { this.seance = seance; }
    public boolean isJustifiee() { return justifiee; }
    public void setJustifiee(boolean justifiee) { this.justifiee = justifiee; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public LocalDateTime getDateDeclaration() { return dateDeclaration; }
    public void setDateDeclaration(LocalDateTime dateDeclaration) { this.dateDeclaration = dateDeclaration; }
    public Justificatif getJustificatif() { return justificatif; }
    public void setJustificatif(Justificatif justificatif) {
        this.justificatif = justificatif;
        if (justificatif != null) {
            justificatif.setAbsence(this);
        }
    }

    public void justifier(String motif, Justificatif justificatif) {
        this.justifiee = true;
        this.motif = motif;
        this.setJustificatif(justificatif);
    }
}