package com.aguardiantes.azarcafetero.auth_service.infrastructure.security;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtValidatorPort jwtValidator;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void shouldAllowPublicPathWithoutToken() throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        request.setRequestURI("/auth/google");

        jwtAuthFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldReturnUnauthorizedWhenAuthorizationHeaderIsMissing()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        request.setRequestURI("/private");

        jwtAuthFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString()
                .contains("Token ausente"));

        verify(filterChain, never())
                .doFilter(any(), any());
    }

    @Test
    void shouldReturnUnauthorizedWhenTokenIsInvalid()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        request.setRequestURI("/private");
        request.addHeader(
                "Authorization",
                "Bearer invalid-token"
        );

        when(jwtValidator.isValid("invalid-token"))
                .thenReturn(false);

        jwtAuthFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        assertEquals(401, response.getStatus());

        assertTrue(response.getContentAsString()
                .contains("Token inválido o expirado"));

        verify(filterChain, never())
                .doFilter(any(), any());
    }

    @Test
    void shouldAllowRequestWhenTokenIsValid()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        request.setRequestURI("/private");
        request.addHeader(
                "Authorization",
                "Bearer valid-token"
        );

        when(jwtValidator.isValid("valid-token"))
                .thenReturn(true);

        when(jwtValidator.extractUserId("valid-token"))
                .thenReturn("user-123");

        jwtAuthFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        assertEquals(
                "user-123",
                request.getAttribute("userId")
        );

        verify(filterChain)
                .doFilter(request, response);
    }

    @Test
    void shouldRejectAuthorizationHeaderWithoutBearer()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        request.setRequestURI("/private");
        request.addHeader(
                "Authorization",
                "Basic abc123"
        );

        jwtAuthFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        assertEquals(401, response.getStatus());

        assertTrue(response.getContentAsString()
                .contains("Token ausente"));

        verify(filterChain, never())
                .doFilter(any(), any());
    }
}