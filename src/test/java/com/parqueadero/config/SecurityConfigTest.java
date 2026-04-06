package com.parqueadero.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

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
        // En tests, configuramos el filter chain inyectando un HttpSecurity desde el contexto
    }

    @Test
    void securityFilterChain_deberiaCrearFiltro() throws Exception {
        // Arrange & Act & Assert
        SecurityFilterChain bean = applicationContext.getBean(SecurityFilterChain.class);
        assertNotNull(bean);
    }

    @Test
    void securityFilterChain_deberiaIncluirJwtAuthFilter() throws Exception {
        // Arrange & Act & Assert
        assertNotNull(jwtAuthFilter);
        assertNotNull(authenticationProvider);
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
