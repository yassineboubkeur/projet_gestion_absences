package com.example.projet_gestion_absences.model.dto;

//package com.example.projet_gestion_absences.dto;

import lombok.Data;

@Data
public class CoursDTO {
    private String code;
    private String intitule;
    private String description;
    private Integer coefficient;
    private Integer volumeHoraire;
    private Long matiereId;
}