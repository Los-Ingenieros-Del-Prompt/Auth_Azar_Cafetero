package com.aguardiantes.azarcafetero.auth_service.infrastructure.adapter;

import com.aguardiantes.azarcafetero.auth_service.domain.model.User;
import com.aguardiantes.azarcafetero.auth_service.domain.port.out.TokenGeneratorPort; // ✅ Puerto correcto
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
// ✅ Implementa TokenGeneratorPort (antes implementaba JwtTokenPort que no existe)
public class JwtTokenAdapter implements TokenGeneratorPort {

    // ✅ Secreto y expiración inyectados desde application.properties
    private final String secret;
    private final long expirationMs;

    public JwtTokenAdapter(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms:86400000}") long expirationMs
    ) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    @Override
    // ✅ Método generateToken() coincide exactamente con el puerto TokenGeneratorPort
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("email", user.getEmail().value())
                .claim("name", user.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}