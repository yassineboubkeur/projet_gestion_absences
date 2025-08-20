package com.example.projet_gestion_absences.model.dto;

// src/main/java/.../model/dto/SeanceResponseDTO.java
//package com.example.projet_gestion_absences.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record SeanceResponseDTO(
        Long id,
        LocalDate date,
        LocalTime heureDebut,
        LocalTime heureFin,
        String statut,          // PLANIFIEE / EFFECTUEE / ANNULEE / REPORTEE
        Long coursId,
        Long professeurId,
        Long salleId,
        Long emploiDuTempsId
) {}
