package com.example.projet_gestion_absences.model.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GenerationRequest {
    private Long classeId;
    private LocalDate startDate; // Ex: "2025-09-01" (lundi)
    private LocalDate endDate;   // Ex: "2025-09-05" (vendredi)
}
