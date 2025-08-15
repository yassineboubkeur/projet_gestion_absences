package com.example.projet_gestion_absences.model.dto;

//package com.example.projet_gestion_absences.dto;

import com.example.projet_gestion_absences.model.dto.EtudiantResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class ClasseResponseDTO {
    private Long id;
    private String nom;
    private String niveau;
    private List<EtudiantResponseDTO> etudiants;
    private Integer effectif; // Number of students
}