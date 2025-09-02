package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.dto.AbsenceDTO;
import com.example.projet_gestion_absences.model.dto.AbsenceResponseDTO;
import com.example.projet_gestion_absences.model.entity.Absence;
import com.example.projet_gestion_absences.model.entity.Etudiant;
import com.example.projet_gestion_absences.model.entity.Seance;
import com.example.projet_gestion_absences.repository.AbsenceRepository;
import com.example.projet_gestion_absences.repository.EtudiantRepository;
import com.example.projet_gestion_absences.repository.SeanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AbsenceService {

    private final AbsenceRepository absenceRepo;
    private final SeanceRepository seanceRepo;
    private final EtudiantRepository etudiantRepo;

    public AbsenceService(AbsenceRepository a, SeanceRepository s, EtudiantRepository e) {
        this.absenceRepo = a;
        this.seanceRepo = s;
        this.etudiantRepo = e;
    }

    @Transactional(readOnly = true)
    public List<AbsenceResponseDTO> listBySeance(Long seanceId) {
        return absenceRepo.findBySeanceId(seanceId).stream().map(a -> {
            AbsenceResponseDTO dto = new AbsenceResponseDTO();
            dto.setId(a.getId());
            dto.setEtudiantId(a.getEtudiant().getId());
            // 🔧 ICI : on mappe 'justifiee' (entité) vers 'justifie' (DTO)
            dto.setJustifie(a.isJustifiee());
            dto.setMotif(a.getMotif());
            return dto;
        }).toList();
    }

    @Transactional(readOnly = true)
    public long countBySeance(Long seanceId) {
        return absenceRepo.countBySeanceId(seanceId);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> statsBySeance(Long seanceId) {
        long total = absenceRepo.countBySeanceId(seanceId);
        long justifiees = absenceRepo.countBySeanceIdAndJustifieeTrue(seanceId);
        long nonJustifiees = total - justifiees;
        return Map.of(
                "total", total,
                "justifiees", justifiees,
                "nonJustifiees", nonJustifiees
        );
    }
    @Transactional(readOnly = true)
    public long countJustifieesBySeance(Long seanceId) {
        return absenceRepo.countBySeanceIdAndJustifieeTrue(seanceId);
    }

    /**
     * Remplace l'état des absences d'une séance :
     *  - supprime les absences non listées
     *  - crée/MAJ celles listées
     * Les étudiants non listés = présents.
     */
    public void saveBulk(Long seanceId, List<AbsenceDTO> payload) {
        Seance seance = seanceRepo.findById(seanceId)
                .orElseThrow(() -> new IllegalArgumentException("Séance introuvable"));

        // Index des absences existantes
        var existing = absenceRepo.findBySeanceId(seanceId);
        var keepIds = payload.stream()
                .map(AbsenceDTO::getEtudiantId)
                .distinct()
                .toList();

        // Supprimer celles qui ne sont plus cochées
        existing.stream()
                .filter(a -> !keepIds.contains(a.getEtudiant().getId()))
                .forEach(absenceRepo::delete);

        // Upsert pour chaque entrée cochée
        for (AbsenceDTO in : payload) {
            Etudiant etu = etudiantRepo.findById(in.getEtudiantId())
                    .orElseThrow(() -> new IllegalArgumentException("Etudiant introuvable: " + in.getEtudiantId()));

            Absence a = absenceRepo.findBySeanceIdAndEtudiantId(seanceId, in.getEtudiantId())
                    .orElseGet(() -> {
                        Absence x = new Absence();
                        x.setSeance(seance);
                        x.setEtudiant(etu);
                        return x;
                    });

            // 🔧 DTO booléen = 'justifie', entité = 'justifiee'
            a.setJustifiee(in.isJustifie());
            a.setMotif(in.getMotif());
            absenceRepo.save(a);
        }
    }
}
