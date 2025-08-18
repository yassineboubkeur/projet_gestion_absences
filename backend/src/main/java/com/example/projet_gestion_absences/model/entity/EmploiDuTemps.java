package com.example.projet_gestion_absences.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "emploi_du_temps")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmploiDuTemps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Par ex: "EDT classe23 (2025-09-01 → 2025-09-08)" */
    private String intitule;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private boolean actif = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "classe_id")
    private Classe classe;

    @OneToMany(mappedBy = "emploiDuTemps", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("edt-seances")
    private List<Seance> seances = new ArrayList<>(); // 👈 IMPORTANT: initialisée

    public EmploiDuTemps() { }

    public EmploiDuTemps(String intitule, Classe classe, LocalDate dateDebut, LocalDate dateFin) {
        this.intitule = intitule;
        this.classe = classe;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.actif = true;
    }

    /* ---------- helpers ---------- */
    public void ajouterSeance(Seance seance) {
        if (seance == null) return;
        // s'assurer que la liste n'est jamais null (défense supplémentaire)
        if (this.seances == null) this.seances = new ArrayList<>();
        this.seances.add(seance);
        seance.setEmploiDuTemps(this);
    }

    /* ---------- getters/setters ---------- */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
}
