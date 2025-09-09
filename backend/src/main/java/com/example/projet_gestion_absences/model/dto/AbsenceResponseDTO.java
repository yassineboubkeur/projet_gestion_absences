package com.example.projet_gestion_absences.model.dto;

//package com.example.projet_gestion_absences.model.dto;

import java.time.LocalDate;

public class AbsenceResponseDTO {
    private Long id;
    private Long etudiantId;
    private boolean justifie;
    private String motif;
    private Long seanceId;
    private LocalDate dateDeclaration;
    private String coursIntitule;
//    private String coursIntitule;
// getters/se

    public String getCoursIntitule() {
        return coursIntitule;
    }

    public void setCoursIntitule(String coursIntitule) {
        this.coursIntitule = coursIntitule;
    }

    public LocalDate getDateDeclaration() {
        return dateDeclaration;
    }

    public void setDateDeclaration(LocalDate dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public Long getSeanceId() {
        return seanceId;
    }

    public void setSeanceId(Long seanceId) {
        this.seanceId = seanceId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public void setJustifie(boolean justifie) {
        this.justifie = justifie;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Long getId() {
        return id;
    }

    public Long getEtudiantId() {
        return etudiantId;
    }

    public boolean isJustifie() {
        return justifie;
    }

    public String getMotif() {
        return motif;
    }
}