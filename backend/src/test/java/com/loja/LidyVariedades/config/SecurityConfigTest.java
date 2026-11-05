package com.loja.LidyVariedades.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecurityConfigTest {

    @Test
    void allowsBothLocalFrontendOrigins() {
        String secondOrigin = "http://127.0.0.1:5173";
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/configuracoes");
        CorsConfiguration cors = new SecurityConfig()
            .corsConfigurationSource("http://localhost:5173," + secondOrigin)
            .getCorsConfiguration(request);

        assertEquals(secondOrigin, cors.checkOrigin(secondOrigin));
        assertEquals(
            "https://aplicacaoweb-eight.vercel.app",
            cors.checkOrigin("https://aplicacaoweb-eight.vercel.app")
        );
    }
}
