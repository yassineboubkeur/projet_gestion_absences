// src/main/java/com/example/projet_gestion_absences/controller/BrandingController.java
package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.dto.settings.BrandingDto;
import com.example.projet_gestion_absences.model.dto.settings.BrandingUpdate;
import com.example.projet_gestion_absences.service.AppSettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','PROFESSEUR')")

public class BrandingController {

    private final AppSettingService settings;

    @GetMapping("/branding")
    public BrandingDto getBranding() {
        return settings.getBranding();
    }

    @PreAuthorize("hasAnyRole('ADMIN')") // équivaut à hasAuthority('ROLE_ADMIN')
    @PutMapping("/branding")
    public BrandingDto updateBranding(@Valid @RequestBody BrandingUpdate req) {
        return settings.updateBranding(req);
    }
}
