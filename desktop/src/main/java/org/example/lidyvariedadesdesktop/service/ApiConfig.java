package org.example.lidyvariedadesdesktop.service;

import java.net.URI;
import java.util.Locale;

public final class ApiConfig {

    private static final String DEFAULT_BASE_URL = "https://lidy-variedades.onrender.com";
    private static final String BASE_URL = loadBaseUrl();

    private ApiConfig() {}

    public static String baseUrl() {
        return BASE_URL;
    }

    public static URI uri() {
        return URI.create(BASE_URL);
    }

    public static boolean isLocal() {
        String host = uri().getHost();
        return host != null && switch (host.toLowerCase(Locale.ROOT)) {
            case "localhost", "127.0.0.1", "::1", "[::1]" -> true;
            default -> false;
        };
    }

    private static String loadBaseUrl() {
        String configuredUrl = System.getenv("LIDY_API_URL");
        String baseUrl = configuredUrl == null || configuredUrl.isBlank()
            ? DEFAULT_BASE_URL
            : configuredUrl.trim();

        while (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        URI uri = URI.create(baseUrl);
        if (!("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))
            || uri.getHost() == null) {
            throw new IllegalArgumentException(
                "LIDY_API_URL deve ser uma URL http ou https válida."
            );
        }
        return baseUrl;
    }
}
