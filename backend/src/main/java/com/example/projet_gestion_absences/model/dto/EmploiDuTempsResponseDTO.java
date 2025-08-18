package com.example.projet_gestion_absences.model.dto;

//package com.example.projet_gestion_absences.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EmploiDuTempsResponseDTO {
    public Long id;
    public String intitule;
    public LocalDate dateDebut;
    public LocalDate dateFin;
    public boolean actif;
    public ClasseLiteDTO classe;
    public List<SeanceResponseDTO> seances;

    public static class ClasseLiteDTO {
        public Long id;
        public String nom;
        public String niveau;
    }

    public static class SeanceResponseDTO {
        public Long id;
        public LocalDate date;
        public LocalTime heureDebut;
        public LocalTime heureFin;
        public String statut; // PLANIFIEE / EFFECTUEE / ANNULEE / REPORTEE

        public CoursLiteDTO cours;
        public ProfesseurLiteDTO professeur;
        public SalleLiteDTO salle;
    }

    public static class CoursLiteDTO {
        public Long id;
        public String code;
        public String intitule;
    }

    public static class ProfesseurLiteDTO {
        public Long id;
        public String nom;
        public String prenom;
    }

    public static class SalleLiteDTO {
        public Long id;
        public String code;   // si tu as 'code' ; sinon laisse null
        public String nom;    // si tu as 'nom' ; sinon laisse null
    }
}

