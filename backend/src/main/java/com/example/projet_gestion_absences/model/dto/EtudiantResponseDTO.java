package com.example.projet_gestion_absences.model.dto;

//package com.example.projet_gestion_absences.dto;

import com.example.projet_gestion_absences.model.entity.Classe;
import lombok.Data;

@Data
public class EtudiantResponseDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String matricule;
    private String address;
    private Classe classe;
}