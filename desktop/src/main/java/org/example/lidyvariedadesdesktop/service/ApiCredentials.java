package org.example.lidyvariedadesdesktop.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

final class ApiCredentials {

    private ApiCredentials() {}

    static String headerValue() {
        String email = System.getenv("ADMIN_EMAIL");
        String password = System.getenv("ADMIN_PASSWORD");
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalStateException(
                "Configure ADMIN_EMAIL e ADMIN_PASSWORD no ambiente para usar o aplicativo desktop."
            );
        }

        String credentials = email.trim() + ":" + password;
        return Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }
}
