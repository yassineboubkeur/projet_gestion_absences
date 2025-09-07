// src/main/java/com/example/projet_gestion_absences/service/AppSettingService.java
package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.dto.settings.BrandingDto;
import com.example.projet_gestion_absences.model.dto.settings.BrandingUpdate;
import com.example.projet_gestion_absences.model.entity.AppSetting;
import com.example.projet_gestion_absences.repository.AppSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppSettingService {

    private final AppSettingRepository repo;

    private static final String BRAND_CODE_KEY  = "brand.shortCode";
    private static final String BRAND_TITLE_KEY = "brand.title";

    @Transactional(readOnly = true)
    public BrandingDto getBranding() {
        BrandingDto dto = new BrandingDto();
        dto.setShortCode(repo.findByKey(BRAND_CODE_KEY).map(AppSetting::getValue).orElse("GA"));
        dto.setTitle(repo.findByKey(BRAND_TITLE_KEY).map(AppSetting::getValue).orElse("Gestion Absences"));
        return dto;
    }

    @Transactional
    public BrandingDto updateBranding(BrandingUpdate req) {
        upsert(BRAND_CODE_KEY, req.getShortCode().trim());
        upsert(BRAND_TITLE_KEY, req.getTitle().trim());
        return getBranding();
    }

    private void upsert(String key, String value) {
        AppSetting s = repo.findByKey(key).orElseGet(() -> AppSetting.builder().key(key).build());
        s.setValue(value);
        repo.save(s);
    }
}
