// src/main/java/com/example/projet_gestion_absences/model/dto/absence/AbsenceResponseDTO.java
package com.example.projet_gestion_absences.model.dto.absence;

public record AbsenceResponseDTO(
        Long id,
        Long etudiantId,
        String etudiantNom,
        String etudiantPrenom,
        boolean justifiee,
        String motif
) {}
