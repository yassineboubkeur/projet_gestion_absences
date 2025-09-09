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

import java.time.LocalDate;
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

    /* ===================== Mapping helper ===================== */
    private AbsenceResponseDTO toDto(Absence a) {
        AbsenceResponseDTO dto = new AbsenceResponseDTO();
        dto.setId(a.getId());

        // Étudiant
        dto.setEtudiantId(a.getEtudiant() != null ? a.getEtudiant().getId() : null);

        // Séance + Cours
        Long seanceId = (a.getSeance() != null ? a.getSeance().getId() : null);
        dto.setSeanceId(seanceId);

        String coursIntitule = (a.getSeance() != null && a.getSeance().getCours() != null)
                ? a.getSeance().getCours().getIntitule()
                : null;
        dto.setCoursIntitule(coursIntitule);

        // Champs simples
        dto.setJustifie(a.isJustifiee());
        dto.setMotif(a.getMotif());

        // LocalDate attendu côté DTO (pas de toLocalDate())
        dto.setDateDeclaration(a.getDateDeclaration());

        return dto;
    }

    /* ===================== Listages ===================== */

    @Transactional(readOnly = true)
    public List<AbsenceResponseDTO> listAll() {
        // Conseillé: @EntityGraph sur absenceRepo.findAll() pour éviter lazy issues
        return absenceRepo.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AbsenceResponseDTO> listByEtudiant(Long etudiantId) {
        // Si tu as un repo dédié: absenceRepo.findByEtudiantId(etudiantId)
        return absenceRepo.findAll().stream()
                .filter(a -> a.getEtudiant() != null && etudiantId.equals(a.getEtudiant().getId()))
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AbsenceResponseDTO> listBySeance(Long seanceId) {
        // Conseillé: @EntityGraph sur absenceRepo.findBySeanceId(seanceId)
        return absenceRepo.findBySeanceId(seanceId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /* ===================== Stats ===================== */

    @Transactional(readOnly = true)
    public long countBySeance(Long seanceId) {
        return absenceRepo.countBySeanceId(seanceId);
    }

    @Transactional(readOnly = true)
    public long countJustifieesBySeance(Long seanceId) {
        return absenceRepo.countBySeanceIdAndJustifieeTrue(seanceId);
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

    /* ===================== Bulk save ===================== */

    /**
     * Remplace l'état des absences d'une séance :
     *  - supprime les absences non listées
     *  - crée/MAJ celles listées (les étudiants listés = absents)
     * Les étudiants non listés = présents.
     */
    public void saveBulk(Long seanceId, List<AbsenceDTO> payload) {
        Seance seance = seanceRepo.findById(seanceId)
                .orElseThrow(() -> new IllegalArgumentException("Séance introuvable"));

        // Index des absences existantes de cette séance
        var existing = absenceRepo.findBySeanceId(seanceId);

        // Identifiants à conserver (absences cochées / transmises)
        var keepIds = payload.stream()
                .map(AbsenceDTO::getEtudiantId)
                .distinct()
                .toList();

        // Supprimer celles qui ne sont plus cochées
        existing.stream()
                .filter(a -> a.getEtudiant() != null && !keepIds.contains(a.getEtudiant().getId()))
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
                        // si la date de déclaration est gérée côté entité, OK.
                        // sinon on peut initialiser ici si null:
                        x.setDateDeclaration(LocalDate.now());
                        return x;
                    });

            // Synchroniser champs
            a.setJustifiee(in.isJustifie()); // DTO: justifie -> entité: justifiee
            a.setMotif(in.getMotif());

            // si tu veux écraser la date de déclaration transmise côté DTO (si tu l’ajoutes plus tard),
            // ajoute un champ dans AbsenceDTO et set ici.
            if (a.getDateDeclaration() == null) {
                a.setDateDeclaration(LocalDate.now());
            }

            absenceRepo.save(a);
        }
    }
}
