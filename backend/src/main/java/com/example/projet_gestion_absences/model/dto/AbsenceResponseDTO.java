package com.example.projet_gestion_absences.model.dto;

//package com.example.projet_gestion_absences.model.dto;

public class AbsenceResponseDTO {
    private Long id;
    private Long etudiantId;
    private boolean justifie;
    private String motif;
// getters/se

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