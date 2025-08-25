package com.example.projet_gestion_absences.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;

@Data
public class MatiereDTO {
    @NotBlank
    @Size(max = 20)
    private String code;

    @NotBlank
    @Size(max = 100)
    private String intitule;

    @Size(max = 50)
    private String domaine;
}

