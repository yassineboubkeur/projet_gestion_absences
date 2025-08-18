// src/main/java/com/example/projet_gestion_absences/model/dto/GenerateEdtRequest.java
package com.example.projet_gestion_absences.model.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class GenerateEdtRequest {
    @NotNull public Long classeId;
    @NotNull public LocalDate startDate;
    @NotNull public LocalDate endDate;
}
