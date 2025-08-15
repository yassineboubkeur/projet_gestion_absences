package com.example.projet_gestion_absences.model.dto;

//package com.example.projet_gestion_absences.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EtudiantDTO {
    @NotBlank private String nom;
    @NotBlank private String prenom;
    @Email private String email;
    @NotBlank private String login;
    @NotBlank @Size(min = 6) private String password;
    @NotBlank private String matricule;
    private String address;
    @NotNull private Long classeId;
}