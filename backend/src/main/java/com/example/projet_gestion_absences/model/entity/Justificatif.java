package com.example.projet_gestion_absences.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Justificatif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cheminFichier;
    private String typeFichier;
    private LocalDateTime dateDepot;
    private String commentaire;

    @OneToOne
    @JoinColumn(name = "absence_id")
    private Absence absence;

    @Enumerated(EnumType.STRING)
    private StatutJustificatif statut = StatutJustificatif.EN_ATTENTE;

    public enum StatutJustificatif {
        EN_ATTENTE,
        VALIDE,
        REJETE
    }

    // Constructors, Getters and Setters
    public Justificatif() {
        this.dateDepot = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }
    public String getTypeFichier() { return typeFichier; }
    public void setTypeFichier(String typeFichier) { this.typeFichier = typeFichier; }
    public LocalDateTime getDateDepot() { return dateDepot; }
    public void setDateDepot(LocalDateTime dateDepot) { this.dateDepot = dateDepot; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public Absence getAbsence() { return absence; }
    public void setAbsence(Absence absence) { this.absence = absence; }
    public StatutJustificatif getStatut() { return statut; }
    public void setStatut(StatutJustificatif statut) { this.statut = statut; }
}