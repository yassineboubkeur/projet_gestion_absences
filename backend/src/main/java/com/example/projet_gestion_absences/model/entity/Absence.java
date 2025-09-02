package com.example.projet_gestion_absences.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "absences",
        uniqueConstraints = @UniqueConstraint(columnNames = {"seance_id", "etudiant_id"})
)
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Un étudiant peut avoir au plus UNE absence pour une séance donnée
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "etudiant_id", nullable = false)
    @JsonBackReference // évite la boucle JSON si tu sérialises l'absence
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seance_id", nullable = false)
    @JsonBackReference
    private Seance seance;

    // NOTE: on garde ton nom de champ 'justifiee' (boolean)
    private boolean justifiee;

    @Column(length = 1000)
    private String motif;

    private LocalDateTime dateDeclaration;

    @OneToOne(mappedBy = "absence", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Justificatif justificatif;

    // Constructors
    public Absence() {
        this.dateDeclaration = LocalDateTime.now();
        this.justifiee = false;
    }

    public Absence(Etudiant etudiant, Seance seance) {
        this();
        this.etudiant = etudiant;
        this.seance = seance;
    }

    // Helper
    public void justifier(String motif, Justificatif justificatif) {
        this.justifiee = true;
        this.motif = motif;
        setJustificatif(justificatif);
    }

    // Getters / Setters
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

    @PrePersist
    public void onCreate() {
        if (this.dateDeclaration == null) {
            this.dateDeclaration = LocalDateTime.now();
        }
    }
}
