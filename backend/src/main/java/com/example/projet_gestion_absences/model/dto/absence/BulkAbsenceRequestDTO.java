// src/main/java/com/example/projet_gestion_absences/model/dto/absence/BulkAbsenceRequestDTO.java
package com.example.projet_gestion_absences.model.dto.absence;

import java.util.List;

public record BulkAbsenceRequestDTO(List<BulkAbsenceItemDTO> absences) { }
