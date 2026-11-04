package com.loja.LidyVariedades.controller;

import com.loja.LidyVariedades.model.StoreSettings;
import com.loja.LidyVariedades.model.StoreSettingsRequest;
import com.loja.LidyVariedades.repository.StoreSettingsRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StoreSettingsControllerTest {

    @Test
    void createsDefaultSettingsWhenNoneExist() {
        StoreSettingsRepository repository = mock(StoreSettingsRepository.class);
        when(repository.findById(1L)).thenReturn(Optional.empty());
        when(repository.save(any(StoreSettings.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        StoreSettingsController controller = new StoreSettingsController(repository);

        StoreSettings settings = controller.getSettings();

        assertEquals(1L, settings.getId());
        assertEquals("5588981137253", settings.getWhatsappNumber());
        assertFalse(settings.isBannerEnabled());
    }

    @Test
    void updatesExistingSettings() {
        StoreSettingsRepository repository = mock(StoreSettingsRepository.class);
        StoreSettings existing = new StoreSettings();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(StoreSettings.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        StoreSettingsRequest request = new StoreSettingsRequest();
        request.setWhatsappNumber("5588999999999");
        request.setBannerEnabled(true);
        request.setBannerText("Frete grátis");
        request.setInstagramProfile("@minhaloja");
        request.setInstagramUrl("https://www.instagram.com/minhaloja/");
        StoreSettingsController controller = new StoreSettingsController(repository);

        StoreSettings settings = controller.updateSettings(request);

        assertEquals("5588999999999", settings.getWhatsappNumber());
        assertEquals("Frete grátis", settings.getBannerText());
        assertEquals("@minhaloja", settings.getInstagramProfile());
        assertEquals("https://www.instagram.com/minhaloja/", settings.getInstagramUrl());
    }
}
