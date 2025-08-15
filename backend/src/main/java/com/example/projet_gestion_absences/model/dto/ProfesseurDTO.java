package com.example.projet_gestion_absences.model.dto;

//package com.example.projet_gestion_absences.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfesseurDTO {
    @NotBlank
    @Size(max = 50)
    private String nom;

    @NotBlank
    @Size(max = 50)
    private String prenom;

    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(max = 50)
    private String login;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    @Size(max = 20)
    private String matricule;

    @NotBlank @Size(max = 100)
    private String specialite;

    @NotBlank @Size(max = 200)
    private String adresse;

    @Past
    private LocalDate dateNaissance;
}