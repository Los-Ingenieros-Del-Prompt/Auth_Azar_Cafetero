package com.aguardiantes.azarcafetero.auth_service.infrastructure.config;

import com.aguardiantes.azarcafetero.auth_service.infrastructure.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// FIX: FilterRegistrationBean<JwtAuthFilter> eliminado.
// Razón: registrar el filtro tanto vía FilterRegistrationBean como vía SecurityFilterChain
// lo ejecutaba dos veces por request. Ahora el filtro se registra exclusivamente
// en SecurityFilterChain con addFilterBefore(), que es el mecanismo correcto en Spring Security.
//
// Adicionalmente se migra la configuración de seguridad a SecurityFilterChain,
// dejando de depender del filtro servlet crudo para autorizar rutas públicas.
// Las rutas públicas se declaran en authorizeHttpRequests(), que es la forma idiomática
// de Spring Security y funciona correctamente con el filtro JWT.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/google").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean("securityCorsConfigurer")
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
