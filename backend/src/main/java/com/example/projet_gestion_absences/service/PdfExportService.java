package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.entity.EmploiDuTemps;
import com.example.projet_gestion_absences.model.entity.Etudiant;
import com.example.projet_gestion_absences.model.entity.Seance;
import com.example.projet_gestion_absences.repository.EmploiDuTempsRepository;
import com.example.projet_gestion_absences.repository.EtudiantRepository;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class PdfExportService {

    private final EtudiantRepository etudiantRepository;
    private final EmploiDuTempsRepository emploiDuTempsRepository;

    public PdfExportService(EtudiantRepository etudiantRepository,
                            EmploiDuTempsRepository emploiDuTempsRepository) {
        this.etudiantRepository = etudiantRepository;
        this.emploiDuTempsRepository = emploiDuTempsRepository;
    }

    /* ================== API ================== */
    public byte[] generateEdtForEtudiant(Long etudiantId) {
        Etudiant etu = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant introuvable: " + etudiantId));

        if (etu.getClasse() == null) {
            throw new IllegalArgumentException("Cet étudiant n'a pas de classe associée.");
        }

        EmploiDuTemps edt = emploiDuTempsRepository
                .findFirstByClasseIdOrderByDateDebutDesc(etu.getClasse().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aucun emploi du temps pour la classe " + etu.getClasse().getNom()
                ));

        return buildEdtPdfForStudent(etu, edt);
    }

    /* ================== Mise en page ================== */

    private static final DayOfWeek[] DAYS = {
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
    };
    private static final String[] DAY_LABELS = {"LUNDI","MARDI","MERCREDI","JEUDI","VENDREDI","SAMEDI"};

    // Grille 8x1h
    private static final String[] SLOT_LABELS = {
            "08:00-09:00","09:00-10:00","10:00-11:00","11:00-12:00",
            "14:00-15:00","15:00-16:00","16:00-17:00","17:00-18:00"
    };
    private static final LocalTime[] SLOT_STARTS = {
            LocalTime.of(8,0), LocalTime.of(9,0), LocalTime.of(10,0), LocalTime.of(11,0),
            LocalTime.of(14,0), LocalTime.of(15,0), LocalTime.of(16,0), LocalTime.of(17,0)
    };
    private static final LocalTime[] SLOT_ENDS = {
            LocalTime.of(9,0), LocalTime.of(10,0), LocalTime.of(11,0), LocalTime.of(12,0),
            LocalTime.of(15,0), LocalTime.of(16,0), LocalTime.of(17,0), LocalTime.of(18,0)
    };

    // Couleurs & styles
    private static final DeviceRgb PURPLE_BG = new DeviceRgb(236, 230, 253);
    private static final DeviceRgb BLUE_BG   = new DeviceRgb(239, 246, 255);
    private static final DeviceRgb BORDER    = new DeviceRgb(209, 213, 219);
    private static final DeviceRgb STATUS_OK = new DeviceRgb(22, 163, 74);
    private static final DeviceRgb STATUS_WARN = new DeviceRgb(202, 138, 4);
    private static final DeviceRgb STATUS_BAD  = new DeviceRgb(220, 38, 38);

    // Dimensions compactes
    private static final float CELL_PADDING = 4f;
    private static final float HEADER_ROW_HEIGHT = 24f;
    private static final float BODY_ROW_HEIGHT   = 28f;

    // Fontes compactes
    private static final float FONT_TITLE   = 12f;
    private static final float FONT_COURSE  = 9f;
    private static final float FONT_DETAILS = 8f;
    private static final float FONT_STATUS  = 7f;

    public byte[] buildEdtPdfForStudent(Etudiant etu, EmploiDuTemps edt) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);

        try (Document doc = new Document(pdf, PageSize.A4.rotate())) {
            // Marges réduites
            doc.setMargins(18, 18, 18, 18);

            String title = String.format(
                    "Emploi du temps — %s %s%s  (%s → %s)",
                    safe(etu.getNom()), safe(etu.getPrenom()),
                    etu.getClasse() != null ? " — " + safe(etu.getClasse().getNom()) : "",
                    fmtDate(edt.getDateDebut()), fmtDate(edt.getDateFin())
            );
            doc.add(new Paragraph(title)
                    .setBold()
                    .setFontSize(FONT_TITLE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMultipliedLeading(0.9f)
                    .setMarginTop(0).setMarginBottom(6).setMarginLeft(0).setMarginRight(0)
            );

            // Préparer les séances par jour
            Map<DayOfWeek, List<Seance>> seancesByDay = new EnumMap<>(DayOfWeek.class);
            for (DayOfWeek d : DAYS) seancesByDay.put(d, new ArrayList<>());
            if (edt.getSeances() != null) {
                for (Seance s : edt.getSeances()) {
                    if (s.getDate() == null) continue;
                    DayOfWeek dow = s.getDate().getDayOfWeek();
                    if (seancesByDay.containsKey(dow)) seancesByDay.get(dow).add(s);
                }
            }

            // Construire la grille d’identités pour fusionner les créneaux adjacents (cours+prof+salle)
            Key[][] grid = buildDayKeyGrid(seancesByDay);

            // Table : 1 colonne créneau + 6 jours
            float[] widths = new float[]{14, 14.333f,14.333f,14.333f,14.333f,14.333f,14.333f};
            Table table = new Table(UnitValue.createPercentArray(widths))
                    .useAllAvailableWidth()
                    .setFixedLayout()
                    .setKeepTogether(true); // tente de garder la table sur une page

            // En-têtes
            table.addHeaderCell(headerCell("Créneau"));
            for (String d : DAY_LABELS) table.addHeaderCell(headerCell(d));

            boolean[][] occupied = new boolean[DAYS.length][SLOT_LABELS.length];

            // Lignes
            for (int r = 0; r < SLOT_LABELS.length; r++) {
                table.addCell(timeCell(SLOT_LABELS[r]));
                for (int c = 0; c < DAYS.length; c++) {
                    if (occupied[c][r]) continue;

                    Key key = grid[c][r];
                    if (key == null) {
                        table.addCell(emptyCell());
                        continue;
                    }

                    // Étendre tant que la clé reste identique (fusion)
                    int span = 1;
                    int rr = r + 1;
                    while (rr < SLOT_LABELS.length && key.equals(grid[c][rr]) && !occupied[c][rr]) {
                        span++;
                        rr++;
                    }
                    for (int k = r; k < r + span; k++) occupied[c][k] = true;

                    // choisir une séance « seed » pour les infos d’affichage
                    Seance seed = findSeedForKey(seancesByDay.get(DAYS[c]), key, SLOT_STARTS[r]);
                    Block block = new Block(seed == null ? anySeanceForKey(seancesByDay.get(DAYS[c]), key) : seed, span);

                    table.addCell(seanceCell(block, span));
                }
            }

            doc.add(table);

            // Légende (minuscule)
            Paragraph legend = new Paragraph("Légende :  • Blocs fusionnés si Cours + Prof + Salle identiques  • Statuts: Planifiée/Effectuée (vert), Reportée (jaune), Annulée (rouge)")
                    .setFontSize(7)
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setMultipliedLeading(0.9f)
                    .setMarginTop(4).setMarginBottom(0).setMarginLeft(0).setMarginRight(0);
            doc.add(legend);
        }

        return baos.toByteArray();
    }

    /* ================== Fusion des créneaux adjacents ================== */

    private static class Key {
        final Long coursId;
        final Long profId;
        final Long salleId;
        Key(Long coursId, Long profId, Long salleId) {
            this.coursId = coursId; this.profId = profId; this.salleId = salleId;
        }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key k)) return false;
            return Objects.equals(coursId, k.coursId)
                    && Objects.equals(profId, k.profId)
                    && Objects.equals(salleId, k.salleId);
        }
        @Override public int hashCode() { return Objects.hash(coursId, profId, salleId); }
        boolean isEmpty() { return coursId == null && profId == null && salleId == null; }
    }

    private static class Block {
        final Seance seed;
        final int parts;
        Block(Seance seed, int parts) { this.seed = seed; this.parts = parts; }
    }

    private Key[][] buildDayKeyGrid(Map<DayOfWeek, List<Seance>> seancesByDay) {
        Key[][] grid = new Key[DAYS.length][SLOT_LABELS.length];
        for (int d = 0; d < DAYS.length; d++) {
            List<Seance> daySeances = seancesByDay.getOrDefault(DAYS[d], List.of());
            for (Seance s : daySeances) {
                if (s.getHeureDebut() == null || s.getHeureFin() == null) continue;

                Key key = new Key(
                        s.getCours() != null ? s.getCours().getId() : null,
                        s.getProfesseur() != null ? s.getProfesseur().getId() : null,
                        s.getSalle() != null ? s.getSalle().getId() : null
                );
                if (key.isEmpty()) continue;

                int idxStart = indexOfSlotStart(s.getHeureDebut());
                int idxEndBoundary = indexOfSlotBoundary(s.getHeureFin());

                // Si non aligné sur la grille, choisir les indices les plus proches
                if (idxStart < 0) idxStart = nearestSlotIndex(s.getHeureDebut());
                if (idxEndBoundary <= idxStart) {
                    idxEndBoundary = Math.min(SLOT_ENDS.length,
                            Math.max(idxStart + 1, idxStart + computeRowSpan(s.getHeureDebut(), s.getHeureFin())));
                }
                idxStart = Math.max(0, Math.min(idxStart, SLOT_LABELS.length - 1));
                idxEndBoundary = Math.max(idxStart + 1, Math.min(idxEndBoundary, SLOT_LABELS.length));

                for (int j = idxStart; j < idxEndBoundary; j++) {
                    if (grid[d][j] == null) grid[d][j] = key; // on garde la première si chevauchement
                }
            }
        }
        return grid;
    }

    private int nearestSlotIndex(LocalTime t) {
        int best = 0; long bestDiff = Long.MAX_VALUE;
        for (int i = 0; i < SLOT_STARTS.length; i++) {
            long diff = Math.abs(java.time.Duration.between(SLOT_STARTS[i], t).toMinutes());
            if (diff < bestDiff) { bestDiff = diff; best = i; }
        }
        return best;
    }

    private Seance findSeedForKey(List<Seance> list, Key key, LocalTime slotStart) {
        if (list == null) return null;
        for (Seance s : list) {
            if (s.getHeureDebut() != null && s.getHeureDebut().equals(slotStart) && sameKey(s, key)) return s;
        }
        for (Seance s : list) if (sameKey(s, key)) return s;
        return null;
    }

    private Seance anySeanceForKey(List<Seance> list, Key key) {
        if (list == null) return null;
        for (Seance s : list) if (sameKey(s, key)) return s;
        return null;
    }

    private boolean sameKey(Seance s, Key k) {
        Long cId = s.getCours() != null ? s.getCours().getId() : null;
        Long pId = s.getProfesseur() != null ? s.getProfesseur().getId() : null;
        Long saId = s.getSalle() != null ? s.getSalle().getId() : null;
        return Objects.equals(cId, k.coursId) && Objects.equals(pId, k.profId) && Objects.equals(saId, k.salleId);
    }

    /* ================== Cellules ================== */

    private Cell headerCell(String text) {
        return new Cell()
                .setMinHeight(HEADER_ROW_HEIGHT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBorder(new SolidBorder(BORDER, 0.4f))
                .setPadding(CELL_PADDING)
                .add(new Paragraph(text).setBold().setFontSize(9f).setMultipliedLeading(0.9f)
                        .setMarginTop(0).setMarginBottom(0).setMarginLeft(0).setMarginRight(0));
    }

    private Cell timeCell(String text) {
        return new Cell()
                .setMinHeight(BODY_ROW_HEIGHT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(BLUE_BG)
                .setBorder(new SolidBorder(BORDER, 0.4f))
                .setPadding(CELL_PADDING)
                .add(new Paragraph(text).setBold().setFontSize(9f).setMultipliedLeading(0.9f)
                        .setMarginTop(0).setMarginBottom(0).setMarginLeft(0).setMarginRight(0));
    }

    private Cell emptyCell() {
        return new Cell()
                .setMinHeight(BODY_ROW_HEIGHT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(new SolidBorder(BORDER, 0.4f))
                .setPadding(CELL_PADDING)
                .add(new Paragraph("—").setFontSize(8f).setMultipliedLeading(0.9f)
                        .setMarginTop(0).setMarginBottom(0).setMarginLeft(0).setMarginRight(0));
    }

    private Cell seanceCell(Block block, int rowSpan) {
        Seance s = block.seed;

        String cours = (s != null && s.getCours() != null)
                ? (safe(s.getCours().getIntitule()) +
                (s.getCours().getClasse() != null ? " " + safe(s.getCours().getClasse().getNom()) : ""))
                : "Cours";

        String details = (s != null ? (s.getProfesseur() != null
                ? (safe(s.getProfesseur().getNom()) + " " + safe(s.getProfesseur().getPrenom()))
                : "—") : "—")
                + " · " + (s != null && s.getSalle() != null ? safe(s.getSalle().getCode()) : "—");

        String statut = statusLabel(s != null ? s.getStatut() : null)
                + (block.parts > 1 ? " • (x" + block.parts + ")" : "");

        DeviceRgb statusColor = statusColor(s != null ? s.getStatut() : null);

        Cell cell = new Cell(rowSpan, 1)
                .setMinHeight(BODY_ROW_HEIGHT * rowSpan)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBackgroundColor(PURPLE_BG)
                .setBorder(new SolidBorder(new DeviceRgb(168, 85, 247), 0.5f))
                .setPadding(CELL_PADDING);

        cell.add(new Paragraph(cours).setBold().setFontSize(FONT_COURSE).setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(0.9f).setMargin(0));
        cell.add(new Paragraph(details).setFontSize(FONT_DETAILS).setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(0.9f).setMargin(0));
        cell.add(new Paragraph(statut).setFontSize(FONT_STATUS).setFontColor(statusColor)
                .setTextAlignment(TextAlignment.CENTER).setMultipliedLeading(0.9f).setMargin(0));

        return cell;
    }

    /* ================== Helpers ================== */

    private String statusLabel(Seance.StatutSeance st) {
        if (st == null) return "PLANIFIÉE";
        return switch (st) {
            case PLANIFIEE -> "PLANIFIÉE";
            case EFFECTUEE -> "EFFECTUÉE";
            case REPORTEE  -> "REPORTÉE";
            case ANNULEE   -> "ANNULÉE";
        };
    }

    private DeviceRgb statusColor(Seance.StatutSeance st) {
        if (st == null) return STATUS_OK;
        return switch (st) {
            case PLANIFIEE, EFFECTUEE -> STATUS_OK;
            case REPORTEE -> STATUS_WARN;
            case ANNULEE  -> STATUS_BAD;
        };
    }

    private int computeRowSpan(LocalTime start, LocalTime end) {
        if (start == null || end == null || !end.isAfter(start)) return 1;
        int idxStart = indexOfSlotStart(start);
        int idxEndBoundary = indexOfSlotBoundary(end);
        if (idxStart < 0 || idxEndBoundary <= idxStart) {
            int mins = (int) java.time.Duration.between(start, end).toMinutes();
            return Math.max(1, (int) Math.round(mins / 60.0));
        }
        return idxEndBoundary - idxStart;
    }

    private int indexOfSlotStart(LocalTime start) {
        for (int i = 0; i < SLOT_STARTS.length; i++) if (SLOT_STARTS[i].equals(start)) return i;
        return -1;
    }

    private int indexOfSlotBoundary(LocalTime end) {
        for (int i = 0; i < SLOT_ENDS.length; i++) if (SLOT_ENDS[i].equals(end)) return i + 1;
        for (int i = 0; i < SLOT_ENDS.length; i++) if (end.isBefore(SLOT_ENDS[i])) return i + 1;
        return SLOT_ENDS.length;
    }

    private String fmtDate(LocalDate d) { return d == null ? "" : d.toString(); }
    private String safe(String s) { return s == null ? "" : s; }
}


//package com.example.projet_gestion_absences.service;
//
//import com.example.projet_gestion_absences.model.entity.EmploiDuTemps;
//import com.example.projet_gestion_absences.model.entity.Etudiant;
//import com.example.projet_gestion_absences.model.entity.Seance;
//import com.example.projet_gestion_absences.repository.EmploiDuTempsRepository;
//import com.example.projet_gestion_absences.repository.EtudiantRepository;
//import com.itextpdf.kernel.colors.ColorConstants;
//import com.itextpdf.kernel.colors.DeviceRgb;
//import com.itextpdf.kernel.geom.PageSize;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.borders.SolidBorder;
//import com.itextpdf.layout.element.Cell;
//import com.itextpdf.layout.element.Paragraph;
//import com.itextpdf.layout.element.Table;
//import com.itextpdf.layout.properties.TextAlignment;
//import com.itextpdf.layout.properties.UnitValue;
//import com.itextpdf.layout.properties.VerticalAlignment;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.*;
//
//@Service
//public class PdfExportService {
//
//    private final EtudiantRepository etudiantRepository;
//    private final EmploiDuTempsRepository emploiDuTempsRepository;
//
//    public PdfExportService(EtudiantRepository etudiantRepository,
//                            EmploiDuTempsRepository emploiDuTempsRepository) {
//        this.etudiantRepository = etudiantRepository;
//        this.emploiDuTempsRepository = emploiDuTempsRepository;
//    }
//
//    /* ================== API ================== */
//
//    public byte[] generateEdtForEtudiant(Long etudiantId) {
//        Etudiant etu = etudiantRepository.findById(etudiantId)
//                .orElseThrow(() -> new IllegalArgumentException("Étudiant introuvable: " + etudiantId));
//
//        if (etu.getClasse() == null) {
//            throw new IllegalArgumentException("Cet étudiant n'a pas de classe associée.");
//        }
//
//        EmploiDuTemps edt = emploiDuTempsRepository
//                .findFirstByClasseIdOrderByDateDebutDesc(etu.getClasse().getId())
//                .orElseThrow(() -> new IllegalArgumentException(
//                        "Aucun emploi du temps pour la classe " + etu.getClasse().getNom()
//                ));
//
//        return buildEdtPdfForStudent(etu, edt);
//    }
//
//    /* ================== Mise en page ================== */
//
//    private static final DayOfWeek[] DAYS = {
//            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
//            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
//    };
//    private static final String[] DAY_LABELS = {"LUNDI","MARDI","MERCREDI","JEUDI","VENDREDI","SAMEDI"};
//
//    // Grille des créneaux (8 blocs d’1h)
//    private static final String[] SLOT_LABELS = {
//            "08:00-09:00","09:00-10:00","10:00-11:00","11:00-12:00",
//            "14:00-15:00","15:00-16:00","16:00-17:00","17:00-18:00"
//    };
//    private static final LocalTime[] SLOT_STARTS = {
//            LocalTime.of(8,0), LocalTime.of(9,0), LocalTime.of(10,0), LocalTime.of(11,0),
//            LocalTime.of(14,0), LocalTime.of(15,0), LocalTime.of(16,0), LocalTime.of(17,0)
//    };
//    private static final LocalTime[] SLOT_ENDS = {
//            LocalTime.of(9,0), LocalTime.of(10,0), LocalTime.of(11,0), LocalTime.of(12,0),
//            LocalTime.of(15,0), LocalTime.of(16,0), LocalTime.of(17,0), LocalTime.of(18,0)
//    };
//
//    // Couleurs & styles
//    private static final DeviceRgb PURPLE_BG = new DeviceRgb(236, 230, 253);
//    private static final DeviceRgb BLUE_BG   = new DeviceRgb(239, 246, 255);
//    private static final DeviceRgb BORDER    = new DeviceRgb(209, 213, 219);
//    private static final DeviceRgb STATUS_OK = new DeviceRgb(22, 163, 74);
//    private static final DeviceRgb STATUS_WARN = new DeviceRgb(202, 138, 4);
//    private static final DeviceRgb STATUS_BAD  = new DeviceRgb(220, 38, 38);
//    private static final float CELL_PADDING = 6f;
//
//    // Hauteurs uniformes
//    private static final float HEADER_ROW_HEIGHT = 30f;
//    private static final float BODY_ROW_HEIGHT   = 42f;
//
//    public byte[] buildEdtPdfForStudent(Etudiant etu, EmploiDuTemps edt) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PdfWriter writer = new PdfWriter(baos);
//        PdfDocument pdf = new PdfDocument(writer);
//
//        try (Document doc = new Document(pdf, PageSize.A4.rotate())) {
//            doc.setMargins(24, 24, 24, 24);
//
//            String title = String.format(
//                    "Emploi du temps — %s %s%s  (%s → %s)",
//                    safe(etu.getNom()), safe(etu.getPrenom()),
//                    etu.getClasse() != null ? " — " + safe(etu.getClasse().getNom()) : "",
//                    fmtDate(edt.getDateDebut()), fmtDate(edt.getDateFin())
//            );
//            doc.add(new Paragraph(title).setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
//            doc.add(new Paragraph(" "));
//
//            // Préparation des données par jour
//            Map<DayOfWeek, List<Seance>> seancesByDay = new EnumMap<>(DayOfWeek.class);
//            for (DayOfWeek d : DAYS) seancesByDay.put(d, new ArrayList<>());
//            if (edt.getSeances() != null) {
//                for (Seance s : edt.getSeances()) {
//                    if (s.getDate() == null) continue;
//                    DayOfWeek dow = s.getDate().getDayOfWeek();
//                    if (seancesByDay.containsKey(dow)) seancesByDay.get(dow).add(s);
//                }
//            }
//            // Grilles « Key » pour détecter et fusionner les créneaux adjacents identiques
//            Key[][] grid = buildDayKeyGrid(seancesByDay);
//
//            // Table
//            float[] widths = new float[]{14, 14.333f,14.333f,14.333f,14.333f,14.333f,14.333f};
//            Table table = new Table(UnitValue.createPercentArray(widths)).useAllAvailableWidth();
//            table.setFixedLayout(); // mise en page stable
//
//            // En-têtes
//            table.addHeaderCell(headerCell("Créneau"));
//            for (String d : DAY_LABELS) table.addHeaderCell(headerCell(d));
//
//            boolean[][] occupied = new boolean[DAYS.length][SLOT_LABELS.length];
//
//            // Lignes
//            for (int r = 0; r < SLOT_LABELS.length; r++) {
//                table.addCell(timeCell(SLOT_LABELS[r]));
//                for (int c = 0; c < DAYS.length; c++) {
//                    if (occupied[c][r]) continue;
//
//                    Key key = grid[c][r];
//                    if (key == null) {
//                        table.addCell(emptyCell());
//                        continue;
//                    }
//
//                    // Étendre tant que la clé est identique : fusion
//                    int span = 1;
//                    int rr = r + 1;
//                    while (rr < SLOT_LABELS.length && key.equals(grid[c][rr]) && !occupied[c][rr]) {
//                        span++;
//                        rr++;
//                    }
//                    for (int k = r; k < r + span; k++) occupied[c][k] = true;
//
//                    // Trouver une séance "seed" pour remplir les infos
//                    Seance seed = findSeedForKey(seancesByDay.get(DAYS[c]), key, SLOT_STARTS[r]);
//                    Block block = new Block(seed == null ? anySeanceForKey(seancesByDay.get(DAYS[c]), key) : seed, span);
//
//                    table.addCell(seanceCell(block, span));
//                }
//            }
//
//            doc.add(table);
//
//            // Légende
//            doc.add(new Paragraph("\nLégende :").setBold().setFontSize(11));
//            doc.add(new Paragraph(
//                    "• Les blocs violets correspondent aux séances ; les créneaux adjacents sont fusionnés " +
//                            "si le cours, le professeur et la salle sont identiques.\n" +
//                            "• Statuts : PLANIFIÉE/EFFECTUÉE (vert), REPORTÉE (jaune), ANNULÉE (rouge)."
//            ).setFontSize(9).setFontColor(ColorConstants.DARK_GRAY));
//        }
//
//        return baos.toByteArray();
//    }
//
//    /* ================== Fusion des créneaux adjacents ================== */
//
//    // Représente l’identité d’un créneau (pour fusion)
//    private static class Key {
//        final Long coursId;
//        final Long profId;
//        final Long salleId;
//
//        Key(Long coursId, Long profId, Long salleId) {
//            this.coursId = coursId;
//            this.profId = profId;
//            this.salleId = salleId;
//        }
//        @Override public boolean equals(Object o) {
//            if (this == o) return true;
//            if (!(o instanceof Key k)) return false;
//            return Objects.equals(coursId, k.coursId)
//                    && Objects.equals(profId, k.profId)
//                    && Objects.equals(salleId, k.salleId);
//        }
//        @Override public int hashCode() { return Objects.hash(coursId, profId, salleId); }
//        boolean isEmpty() { return coursId == null && profId == null && salleId == null; }
//    }
//
//    // Pour afficher infos + compter le nombre de créneaux fusionnés
//    private static class Block {
//        final Seance seed;
//        final int parts;
//        Block(Seance seed, int parts) { this.seed = seed; this.parts = parts; }
//    }
//
//    /**
//     * Construit une grille [dayIndex][slotIndex] de Key.
//     * Si plusieurs séances se chevauchent, la première rencontrée « gagne » ce créneau.
//     * Les séances non alignées exactement sur la grille (heures non prévues) sont arrondies à la limite suivante.
//     */
//    private Key[][] buildDayKeyGrid(Map<DayOfWeek, List<Seance>> seancesByDay) {
//        Key[][] grid = new Key[DAYS.length][SLOT_LABELS.length];
//        for (int d = 0; d < DAYS.length; d++) {
//            List<Seance> daySeances = seancesByDay.getOrDefault(DAYS[d], List.of());
//            for (Seance s : daySeances) {
//                if (s.getHeureDebut() == null || s.getHeureFin() == null) continue;
//
//                Key key = new Key(
//                        s.getCours() != null ? s.getCours().getId() : null,
//                        s.getProfesseur() != null ? s.getProfesseur().getId() : null,
//                        s.getSalle() != null ? s.getSalle().getId() : null
//                );
//                if (key.isEmpty()) continue;
//
//                int idxStart = indexOfSlotStart(s.getHeureDebut());
//                int idxEndBoundary = indexOfSlotBoundary(s.getHeureFin());
//
//                // Si pas aligné, essayer d'estimer une couverture en heures
//                if (idxStart < 0) {
//                    idxStart = nearestSlotIndex(s.getHeureDebut());
//                }
//                if (idxEndBoundary <= idxStart) {
//                    idxEndBoundary = Math.min(SLOT_ENDS.length,
//                            Math.max(idxStart + 1, idxStart + computeRowSpan(s.getHeureDebut(), s.getHeureFin())));
//                }
//
//                idxStart = Math.max(0, Math.min(idxStart, SLOT_LABELS.length - 1));
//                idxEndBoundary = Math.max(idxStart + 1, Math.min(idxEndBoundary, SLOT_LABELS.length));
//
//                for (int j = idxStart; j < idxEndBoundary; j++) {
//                    // n’écrase pas s’il y a déjà quelque chose (on garde la première)
//                    if (grid[d][j] == null) {
//                        grid[d][j] = key;
//                    }
//                }
//            }
//        }
//        return grid;
//    }
//
//    private int nearestSlotIndex(LocalTime t) {
//        int best = 0;
//        long bestDiff = Long.MAX_VALUE;
//        for (int i = 0; i < SLOT_STARTS.length; i++) {
//            long diff = Math.abs(java.time.Duration.between(SLOT_STARTS[i], t).toMinutes());
//            if (diff < bestDiff) { bestDiff = diff; best = i; }
//        }
//        return best;
//    }
//
//    private Seance findSeedForKey(List<Seance> list, Key key, LocalTime slotStart) {
//        if (list == null) return null;
//        // priorité à la séance qui démarre exactement au créneau
//        for (Seance s : list) {
//            if (s.getHeureDebut() != null && s.getHeureDebut().equals(slotStart) && sameKey(s, key)) return s;
//        }
//        // sinon n’importe laquelle avec la même identité
//        for (Seance s : list) if (sameKey(s, key)) return s;
//        return null;
//    }
//
//    private Seance anySeanceForKey(List<Seance> list, Key key) {
//        if (list == null) return null;
//        for (Seance s : list) if (sameKey(s, key)) return s;
//        return null;
//    }
//
//    private boolean sameKey(Seance s, Key k) {
//        Long cId = s.getCours() != null ? s.getCours().getId() : null;
//        Long pId = s.getProfesseur() != null ? s.getProfesseur().getId() : null;
//        Long saId = s.getSalle() != null ? s.getSalle().getId() : null;
//        return Objects.equals(cId, k.coursId) && Objects.equals(pId, k.profId) && Objects.equals(saId, k.salleId);
//    }
//
//    /* ================== Cellules ================== */
//
//    private Cell headerCell(String text) {
//        return new Cell()
//                .setMinHeight(HEADER_ROW_HEIGHT)
//                .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                .setTextAlignment(TextAlignment.CENTER)
//                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
//                .setBorder(new SolidBorder(BORDER, 0.5f))
//                .setPadding(CELL_PADDING)
//                .add(new Paragraph(text).setBold());
//    }
//
//    private Cell timeCell(String text) {
//        return new Cell()
//                .setMinHeight(BODY_ROW_HEIGHT)
//                .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                .setTextAlignment(TextAlignment.CENTER)
//                .setBackgroundColor(BLUE_BG)
//                .setBorder(new SolidBorder(BORDER, 0.5f))
//                .setPadding(CELL_PADDING)
//                .add(new Paragraph(text).setBold());
//    }
//
//    private Cell emptyCell() {
//        return new Cell()
//                .setMinHeight(BODY_ROW_HEIGHT)
//                .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                .setTextAlignment(TextAlignment.CENTER)
//                .setBorder(new SolidBorder(BORDER, 0.5f))
//                .setPadding(CELL_PADDING)
//                .add(new Paragraph("—"));
//    }
//
//    private Cell seanceCell(Block block, int rowSpan) {
//        Seance s = block.seed;
//
//        String line1 = (s != null && s.getCours() != null)
//                ? (safe(s.getCours().getIntitule()) +
//                (s.getCours().getClasse() != null ? " " + safe(s.getCours().getClasse().getNom()) : ""))
//                : "Cours";
//        String line2 = (s != null && s.getProfesseur() != null)
//                ? (safe(s.getProfesseur().getNom()) + " " + safe(s.getProfesseur().getPrenom()))
//                : "—";
//        String line3 = (s != null && s.getSalle() != null) ? safe(s.getSalle().getCode()) : "—";
//
//        Cell cell = new Cell(rowSpan, 1)
//                .setMinHeight(BODY_ROW_HEIGHT * rowSpan)                 // hauteur proportionnelle
//                .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                .setBackgroundColor(PURPLE_BG)
//                .setBorder(new SolidBorder(new DeviceRgb(168, 85, 247), 0.6f))
//                .setPadding(CELL_PADDING);
//
//        cell.add(new Paragraph(line1).setBold().setFontSize(10));
//        cell.add(new Paragraph(line2).setFontSize(9));
//        cell.add(new Paragraph(line3).setFontSize(9));
//
//        DeviceRgb c = statusColor(s != null ? s.getStatut() : null);
//        String extra = block.parts > 1 ? " • (fusionné x" + block.parts + ")" : "";
//        String txt = statusLabel(s != null ? s.getStatut() : null) + extra;
//        cell.add(new Paragraph(txt).setFontSize(9).setFontColor(c));
//
//        return cell;
//    }
//
//    /* ================== Helpers ================== */
//
//    private String statusLabel(Seance.StatutSeance st) {
//        if (st == null) return "PLANIFIÉE";
//        return switch (st) {
//            case PLANIFIEE -> "PLANIFIÉE";
//            case EFFECTUEE -> "EFFECTUÉE";
//            case REPORTEE  -> "REPORTÉE";
//            case ANNULEE   -> "ANNULÉE";
//        };
//    }
//
//    private DeviceRgb statusColor(Seance.StatutSeance st) {
//        if (st == null) return STATUS_OK;
//        return switch (st) {
//            case PLANIFIEE, EFFECTUEE -> STATUS_OK;
//            case REPORTEE -> STATUS_WARN;
//            case ANNULEE  -> STATUS_BAD;
//        };
//    }
//
//    private int computeRowSpan(LocalTime start, LocalTime end) {
//        if (start == null || end == null || !end.isAfter(start)) return 1;
//        int idxStart = indexOfSlotStart(start);
//        int idxEndBoundary = indexOfSlotBoundary(end);
//        if (idxStart < 0 || idxEndBoundary <= idxStart) {
//            int mins = (int) java.time.Duration.between(start, end).toMinutes();
//            return Math.max(1, (int) Math.round(mins / 60.0));
//        }
//        return idxEndBoundary - idxStart;
//    }
//
//    private int indexOfSlotStart(LocalTime start) {
//        for (int i = 0; i < SLOT_STARTS.length; i++) if (SLOT_STARTS[i].equals(start)) return i;
//        return -1;
//    }
//
//    private int indexOfSlotBoundary(LocalTime end) {
//        for (int i = 0; i < SLOT_ENDS.length; i++) if (SLOT_ENDS[i].equals(end)) return i + 1;
//        for (int i = 0; i < SLOT_ENDS.length; i++) if (end.isBefore(SLOT_ENDS[i])) return i + 1;
//        return SLOT_ENDS.length;
//    }
//
//    private String fmtDate(LocalDate d) { return d == null ? "" : d.toString(); }
//    private String safe(String s) { return s == null ? "" : s; }
//}
