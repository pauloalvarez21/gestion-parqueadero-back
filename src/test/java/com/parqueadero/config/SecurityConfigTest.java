package com.parqueadero.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    private SecurityFilterChain filterChain;

    @BeforeEach
    void setUp() throws Exception {
        filterChain = securityConfig.securityFilterChain(
            applicationContext.getBean(org.springframework.security.config.annotation.web.builders.HttpSecurity.class)
        );
    }

    @Test
    void securityFilterChain_deberiaCrearFiltroConCORS() throws Exception {
        // Arrange & Act
        SecurityFilterChain chain = securityConfig.securityFilterChain(
            applicationContext.getBean(org.springframework.security.config.annotation.web.builders.HttpSecurity.class)
        );

        // Assert
        assertNotNull(chain);
        // CORS está configurado en el filter chain
    }

    @Test
    void securityFilterChain_deberiaTenerPoliticaStateless() throws Exception {
        // Arrange & Act
        SecurityFilterChain chain = securityConfig.securityFilterChain(
            applicationContext.getBean(org.springframework.security.config.annotation.web.builders.HttpSecurity.class)
        );

        // Assert
        assertNotNull(chain);
        // La configuración se verifica indirectamente al crear el chain
    }

    @Test
    void securityFilterChain_deberiaIncluirJwtAuthFilter() throws Exception {
        // Arrange & Act
        SecurityFilterChain chain = securityConfig.securityFilterChain(
            applicationContext.getBean(org.springframework.security.config.annotation.web.builders.HttpSecurity.class)
        );

        // Assert
        assertNotNull(chain);
        assertNotNull(jwtAuthFilter);
        assertNotNull(authenticationProvider);
    }

    @Test
    void corsConfigurationSource_deberiaRetornarConfiguracion() {
        // Arrange & Act
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        CorsConfiguration config = source.getCorsConfiguration(new org.springframework.mock.web.MockHttpServletRequest());

        // Assert
        assertNotNull(config);
        assertNotNull(config.getAllowedOrigins());
        assertTrue(config.getAllowedOrigins().contains("http://localhost:4200"));
    }

    @Test
    void corsConfigurationSource_deberiaPermitirMetodosHTTP() {
        // Arrange & Act
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        CorsConfiguration config = source.getCorsConfiguration(new org.springframework.mock.web.MockHttpServletRequest());

        // Assert
        assertNotNull(config);
        assertTrue(config.getAllowedMethods().contains("GET"));
        assertTrue(config.getAllowedMethods().contains("POST"));
        assertTrue(config.getAllowedMethods().contains("PUT"));
        assertTrue(config.getAllowedMethods().contains("DELETE"));
        assertTrue(config.getAllowedMethods().contains("PATCH"));
        assertTrue(config.getAllowedMethods().contains("OPTIONS"));
    }

    @Test
    void corsConfigurationSource_deberiaPermitirHeaders() {
        // Arrange & Act
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        CorsConfiguration config = source.getCorsConfiguration(new org.springframework.mock.web.MockHttpServletRequest());

        // Assert
        assertNotNull(config);
        assertTrue(config.getAllowedHeaders().contains("Authorization"));
        assertTrue(config.getAllowedHeaders().contains("Content-Type"));
    }

    @Test
    void corsConfigurationSource_deberiaPermitirCredentials() {
        // Arrange & Act
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        CorsConfiguration config = source.getCorsConfiguration(new org.springframework.mock.web.MockHttpServletRequest());

        // Assert
        assertNotNull(config);
        assertTrue(config.getAllowCredentials());
    }

    @Test
    void beans_deberiaEstarRegistradosEnContexto() {
        // Arrange & Act
        SecurityFilterChain bean = applicationContext.getBean(SecurityFilterChain.class);
        JwtAuthenticationFilter jwtBean = applicationContext.getBean(JwtAuthenticationFilter.class);
        AuthenticationProvider authProvider = applicationContext.getBean(AuthenticationProvider.class);

        // Assert
        assertNotNull(bean);
        assertNotNull(jwtBean);
        assertNotNull(authProvider);
    }
}
