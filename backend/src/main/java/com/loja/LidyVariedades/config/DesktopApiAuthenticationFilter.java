package com.loja.LidyVariedades.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class DesktopApiAuthenticationFilter extends OncePerRequestFilter {

    public static final String HEADER_NAME = "X-Desktop-Auth";

    private final AuthenticationManager authenticationManager;

    public DesktopApiAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String encodedCredentials = request.getHeader(HEADER_NAME);
        if (encodedCredentials == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String credentials = new String(
                Base64.getDecoder().decode(encodedCredentials),
                StandardCharsets.UTF_8
            );
            int separator = credentials.indexOf(':');
            if (separator <= 0) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            var authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                    credentials.substring(0, separator),
                    credentials.substring(separator + 1)
                )
            );
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        } catch (IllegalArgumentException | AuthenticationException exception) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
