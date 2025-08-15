package com.example.projet_gestion_absences.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class SpecialiteWithProfesseursDTO {
    private String specialite;
    private List<ProfesseurResponseDTO> professeurs;
}
