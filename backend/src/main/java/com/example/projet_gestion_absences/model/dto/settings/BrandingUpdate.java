// src/main/java/com/example/projet_gestion_absences/model/dto/settings/BrandingUpdate.java
package com.example.projet_gestion_absences.model.dto.settings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BrandingUpdate {
    @NotBlank @Size(max = 12)
    private String shortCode;

    @NotBlank @Size(max = 50)
    private String title;
}
