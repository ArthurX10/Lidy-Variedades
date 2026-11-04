package com.loja.LidyVariedades.controller;

import com.loja.LidyVariedades.model.StoreSettings;
import com.loja.LidyVariedades.model.StoreSettingsRequest;
import com.loja.LidyVariedades.repository.StoreSettingsRepository;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/configuracoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class StoreSettingsController {

    private static final long SETTINGS_ID = 1L;
    private final StoreSettingsRepository settingsRepository;

    public StoreSettingsController(StoreSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @GetMapping
    public StoreSettings getSettings() {
        return settingsRepository.findById(SETTINGS_ID)
            .orElseGet(() -> settingsRepository.save(new StoreSettings()));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public StoreSettings updateSettings(@Valid @RequestBody StoreSettingsRequest request) {
        StoreSettings settings = settingsRepository.findById(SETTINGS_ID)
            .orElseGet(StoreSettings::new);
        settings.setWhatsappNumber(request.getWhatsappNumber());
        settings.setBannerEnabled(request.isBannerEnabled());
        settings.setBannerText(request.getBannerText() == null ? "" : request.getBannerText().trim());
        settings.setInstagramProfile(
            request.getInstagramProfile() == null ? "" : request.getInstagramProfile().trim()
        );
        settings.setInstagramUrl(request.getInstagramUrl() == null ? "" : request.getInstagramUrl().trim());
        return settingsRepository.save(settings);
    }
}
