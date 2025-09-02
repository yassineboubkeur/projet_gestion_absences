package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.service.PdfExportService;
import com.example.projet_gestion_absences.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emploi-du-temps")
public class EdtExportController {

    private final PdfExportService pdfExportService;
    private final JwtService jwtService;

    public EdtExportController(PdfExportService pdfExportService, JwtService jwtService) {
        this.pdfExportService = pdfExportService;
        this.jwtService = jwtService;
    }

    @GetMapping(value = "/etudiant/{etudiantId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportEtudiantPdf(
            @PathVariable Long etudiantId,
            HttpServletRequest request
    ) {
        // --- Extract and validate token
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth == null || !auth.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = auth.substring(7).trim();

        // role is like "ROLE_ADMIN" / "ROLE_PROFESSEUR" / "ROLE_ETUDIANT"
        String role = jwtService.extractClaim(token, "role", String.class);
        Long requesterUserId = null;
        Number userIdNum = jwtService.extractClaim(token, "userId", Number.class);
        if (userIdNum != null) requesterUserId = userIdNum.longValue();

        // If it's a student, enforce "only my own PDF"
        if ("ROLE_ETUDIANT".equals(role)) {
            if (requesterUserId == null || !etudiantId.equals(requesterUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        // --- Generate the PDF (make sure your service handles 'no data' gracefully)
        byte[] pdf = pdfExportService.generateEdtForEtudiant(etudiantId);
        if (pdf == null || pdf.length == 0) {
            // Optional: return 204 when nothing to export
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        String filename = "edt-etudiant-" + etudiantId + ".pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}


