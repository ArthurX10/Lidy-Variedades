package com.loja.LidyVariedades.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;

@Configuration
public class SecurityConfig {

    private static final String PRODUCTION_FRONTEND_ORIGIN = "https://aplicacaoweb-eight.vercel.app";

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService adminUserDetailsService(
        PasswordEncoder passwordEncoder,
        @Value("${app.admin.email:}") String adminEmail,
        @Value("${app.admin.password:}") String adminPassword
    ) {
        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            throw new IllegalStateException(
                "Configure ADMIN_EMAIL e ADMIN_PASSWORD no ambiente antes de iniciar o backend."
            );
        }

        return new InMemoryUserDetailsManager(
            User.withUsername(adminEmail.trim())
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build()
        );
    }

    @Bean
    SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        AuthenticationManager authenticationManager,
        @Value("${app.cookie.secure:true}") boolean secureCookies
    ) throws Exception {
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookieCustomizer(cookie -> cookie.sameSite("None").secure(secureCookies));

        http
            .cors(cors -> {})
            .csrf(csrf -> csrf
                .csrfTokenRepository(csrfTokenRepository)
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                .ignoringRequestMatchers(request -> {
                    String desktopCredential = request.getHeader(DesktopApiAuthenticationFilter.HEADER_NAME);
                    return desktopCredential != null && !desktopCredential.isBlank();
                })
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/csrf", "/auth/login").permitAll()
                .requestMatchers(
                    HttpMethod.GET,
                    "/catalogo/produtos/**",
                    "/categorias",
                    "/marcas",
                    "/configuracoes"
                ).permitAll()
                .requestMatchers(HttpMethod.POST, "/vendas").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .formLogin(form -> form.disable())
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> response.setStatus(204))
            )
            .exceptionHandling(errors -> errors
                .authenticationEntryPoint((request, response, exception) -> response.sendError(401))
            )
            .addFilterBefore(
                new DesktopApiAuthenticationFilter(authenticationManager),
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(
        @Value("${app.cors.allowed-origin:https://aplicacaoweb-eight.vercel.app,http://localhost:5173,http://127.0.0.1:5173}") String allowedOrigins
    ) {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> origins = new ArrayList<>(Arrays.stream(allowedOrigins.split(","))
            .map(String::trim)
            .filter(origin -> !origin.isEmpty())
            .toList());
        if (!origins.contains(PRODUCTION_FRONTEND_ORIGIN)) {
            origins.add(PRODUCTION_FRONTEND_ORIGIN);
        }
        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Content-Type", "X-XSRF-TOKEN"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
