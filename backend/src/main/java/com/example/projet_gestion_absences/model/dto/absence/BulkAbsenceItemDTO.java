// src/main/java/com/example/projet_gestion_absences/model/dto/absence/BulkAbsenceItemDTO.java
package com.example.projet_gestion_absences.model.dto.absence;

public record BulkAbsenceItemDTO(Long etudiantId, Boolean justifiee, String motif) { }
