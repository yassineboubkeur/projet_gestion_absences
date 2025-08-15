package com.example.projet_gestion_absences.model.dto;

//package com.example.projet_gestion_absences.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClasseDTO {
    @NotBlank(message = "Class name is required")
    @Size(max = 50, message = "Class name must be less than 50 characters")
    private String nom;

    @NotBlank(message = "Level is required")
    @Size(max = 30, message = "Level must be less than 30 characters")
    private String niveau;
}