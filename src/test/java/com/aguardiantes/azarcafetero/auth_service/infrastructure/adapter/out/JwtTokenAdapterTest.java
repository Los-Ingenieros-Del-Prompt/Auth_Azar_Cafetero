package com.aguardiantes.azarcafetero.auth_service.infrastructure.adapter.out;

import com.aguardiantes.azarcafetero.auth_service.domain.model.User;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.Email;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.GoogleId;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenAdapterTest {

    private static final String SECRET =
            "mySuperSecretKeyThatHasAtLeastThirtyTwoCharacters";

    @Test
    void shouldGenerateValidJwtToken() {

        JwtTokenAdapter adapter = new JwtTokenAdapter(
                SECRET,
                86400000
        );

        User user = User.registerWithGoogle(
                new GoogleId("google-123"),
                new Email("karol@example.com"),
                "Karol",
                "avatar.png"
        );

        String token = adapter.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(
                        Keys.hmacShaKeyFor(
                                SECRET.getBytes(StandardCharsets.UTF_8)
                        )
                )
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("karol@example.com", claims.getSubject());
        assertEquals("karol@example.com", claims.get("email"));
        assertEquals("Karol", claims.get("name"));
        assertEquals("avatar.png", claims.get("avatarUrl"));

        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void shouldContainExpirationDate() {

        long expirationMs = 1000 * 60 * 60;

        JwtTokenAdapter adapter = new JwtTokenAdapter(
                SECRET,
                expirationMs
        );

        User user = User.registerWithGoogle(
                new GoogleId("google-456"),
                new Email("test@example.com"),
                "Test User",
                "test.png"
        );

        String token = adapter.generateToken(user);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(
                        Keys.hmacShaKeyFor(
                                SECRET.getBytes(StandardCharsets.UTF_8)
                        )
                )
                .build()
                .parseClaimsJws(token)
                .getBody();

        long difference =
                claims.getExpiration().getTime()
                        - claims.getIssuedAt().getTime();

        assertTrue(difference <= expirationMs);
        assertTrue(difference > 0);
    }
}