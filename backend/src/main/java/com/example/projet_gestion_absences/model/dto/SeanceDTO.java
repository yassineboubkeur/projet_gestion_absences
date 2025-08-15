package com.example.projet_gestion_absences.model.dto;

import com.example.projet_gestion_absences.model.entity.Seance;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SeanceDTO {
    private Long coursId;
    private Long professeurId;
    private Long salleId;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Seance.StatutSeance statut;
}