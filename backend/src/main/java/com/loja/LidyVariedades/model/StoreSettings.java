package com.loja.LidyVariedades.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "store_settings")
public class StoreSettings {

    @Id
    private Long id = 1L;

    @Column(name = "whatsapp_number", nullable = false, length = 20)
    private String whatsappNumber = "5588981137253";

    @Column(name = "banner_enabled", nullable = false)
    private boolean bannerEnabled;

    @Column(name = "banner_text", nullable = false, length = 500)
    private String bannerText = "";

    @Column(name = "instagram_profile", nullable = false, length = 100)
    private String instagramProfile = "@_lidy.variedades";

    @Column(name = "instagram_url", nullable = false, length = 500)
    private String instagramUrl = "https://www.instagram.com/_lidy.variedades/";

    public StoreSettings() {
    }

    public Long getId() {
        return id;
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
