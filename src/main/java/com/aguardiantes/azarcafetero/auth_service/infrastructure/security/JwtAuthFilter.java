package com.aguardiantes.azarcafetero.auth_service.infrastructure.security;

import com.aguardiantes.azarcafetero.auth_service.infrastructure.security.JwtValidatorPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // Rutas públicas que NO requieren token
    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/google"
    );

    private final JwtValidatorPort jwtValidator;

    public JwtAuthFilter(JwtValidatorPort jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Dejar pasar rutas públicas
        if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorized(response, "Token ausente");
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtValidator.isValid(token)) {
            sendUnauthorized(response, "Token inválido o expirado");
            return;
        }

        // Inyectar userId en el request para que los controllers lo usen
        String userId = jwtValidator.extractUserId(token);
        request.setAttribute("userId", userId);

        chain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("""
                {"status":401,"error":"Unauthorized","message":"%s"}
                """.formatted(message));
    }
}