package com.loja.LidyVariedades.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class StoreSettingsRequest {

    @NotBlank
    @Pattern(regexp = "[0-9]{10,15}", message = "Informe o WhatsApp somente com números.")
    private String whatsappNumber;

    private boolean bannerEnabled;

    @Size(max = 500)
    private String bannerText = "";

    @Size(max = 100)
    private String instagramProfile = "";

    @Size(max = 500)
    @Pattern(regexp = "^$|https?://.+", message = "O link do Instagram deve começar com http:// ou https://.")
    private String instagramUrl = "";

    public StoreSettingsRequest() {
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }

    public boolean isBannerEnabled() {
        return bannerEnabled;
    }

    public void setBannerEnabled(boolean bannerEnabled) {
        this.bannerEnabled = bannerEnabled;
    }

    public String getBannerText() {
        return bannerText;
    }

    public void setBannerText(String bannerText) {
        this.bannerText = bannerText;
    }

    public String getInstagramProfile() {
        return instagramProfile;
    }

    public void setInstagramProfile(String instagramProfile) {
        this.instagramProfile = instagramProfile;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }
}
