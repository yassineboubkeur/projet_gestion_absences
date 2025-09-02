package com.example.projet_gestion_absences.model.dto;

//package com.example.projet_gestion_absences.model.dto;

public class AbsenceDTO {
    private Long etudiantId;
    private boolean justifie;
    private String motif;
    // getters/setters

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public void setJustifie(boolean justifie) {
        this.justifie = justifie;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public boolean isJustifie() {
        return justifie;
    }

    public Long getEtudiantId() {
        return etudiantId;
    }

    public String getMotif() {
        return motif;
    }
}