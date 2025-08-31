package com.example.projet_gestion_absences.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/settings")
public class BackgroundController {

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    // ✅ GET current background (returns latest image)
    @GetMapping("/background")
    public Map<String, String> getBackground() throws IOException {
        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) return Map.of("url", "");

        try (var stream = Files.list(dir)) {
            String last = stream
                    .filter(p -> !Files.isDirectory(p) && p.getFileName().toString().matches("bg-.*\\.(png|jpg|jpeg|webp)$"))
                    .sorted((a, b) -> {
                        try {
                            return Files.getLastModifiedTime(b).compareTo(Files.getLastModifiedTime(a));
                        } catch (IOException e) {
                            return 0;
                        }
                    })
                    .map(p -> "/uploads/" + p.getFileName().toString())
                    .findFirst()
                    .orElse("");
            return Map.of("url", last);
        }
    }

    // ✅ POST upload background (any image format)
    @PostMapping(value = "/background", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Map<String, String> uploadBackground(@RequestPart("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Fichier vide");

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Le fichier doit être une image (PNG, JPG, JPEG, WEBP)");
        }

        Files.createDirectories(Paths.get(uploadDir));

        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (ext == null) ext = "png";
        String filename = "bg-" + UUID.randomUUID() + "." + ext.toLowerCase();
        Path dest = Paths.get(uploadDir).resolve(filename);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

        return Map.of("url", "/uploads/" + filename);
    }

    // ✅ DELETE all background images
    @DeleteMapping("/background")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteBackground() throws IOException {
        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) return;

        try (var stream = Files.list(dir)) {
            stream.filter(p -> !Files.isDirectory(p) && p.getFileName().toString().matches("bg-.*\\.(png|jpg|jpeg|webp)$"))
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException ignored) {}
                    });
        }
    }
}