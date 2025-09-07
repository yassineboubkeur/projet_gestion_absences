// src/main/java/com/example/projet_gestion_absences/model/dto/settings/BrandingDto.java
package com.example.projet_gestion_absences.model.dto.settings;

import lombok.Data;

@Data
public class BrandingDto {
    private String shortCode; // ex: "GA"
    private String title;     // ex: "Gestion Absences"
}
