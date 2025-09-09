package com.example.projet_gestion_absences.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

// Si Utilisateur est en SINGLE_TABLE avec @DiscriminatorColumn:
@DiscriminatorValue("ETUDIANT")
// Si Utilisateur est en JOINED, dé-commente plutôt la ligne suivante :
// @PrimaryKeyJoinColumn(name = "id")
@Entity
public class Etudiant extends Utilisateur {

    private String matricule;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classe_id")
    @JsonIgnore
    private Classe classe;

    public Etudiant() {}

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Classe getClasse() { return classe; }
    public void setClasse(Classe classe) { this.classe = classe; }
}
