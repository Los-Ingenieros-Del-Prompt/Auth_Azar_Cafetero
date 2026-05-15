package com.aguardiantes.azarcafetero.auth_service.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CorsFilter;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    @Test
    void shouldCreateCorsFilterRegistrationBean() {

        CorsConfig corsConfig = new CorsConfig();

        FilterRegistrationBean<CorsFilter> bean =
                corsConfig.corsFilterRegistration();

        assertNotNull(bean);
        assertNotNull(bean.getFilter());

        assertEquals(
                Ordered.HIGHEST_PRECEDENCE,
                bean.getOrder()
        );
    }

    @Test
    void shouldContainCorsFilter() {

        CorsConfig corsConfig = new CorsConfig();

        FilterRegistrationBean<CorsFilter> bean =
                corsConfig.corsFilterRegistration();

        assertInstanceOf(
                CorsFilter.class,
                bean.getFilter()
        );
    }
}