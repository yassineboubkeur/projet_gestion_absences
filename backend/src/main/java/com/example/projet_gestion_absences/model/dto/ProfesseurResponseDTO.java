package com.example.projet_gestion_absences.model.dto;

//package com.example.projet_gestion_absences.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfesseurResponseDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String matricule;
    private String specialite;
    private String adresse;
    private LocalDate dateNaissance;
    private boolean active;

    public int getAge() {
        if (dateNaissance == null) return 0;
        return LocalDate.now().getYear() - dateNaissance.getYear();
    }
}