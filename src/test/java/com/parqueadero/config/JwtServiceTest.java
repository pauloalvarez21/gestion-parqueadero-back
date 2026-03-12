package com.parqueadero.config;

import com.parqueadero.entity.Usuario;
import com.parqueadero.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"; // 256-bit key in Base64
    private final long EXPIRATION = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);
    }

    @Test
    void generateToken_deberiaGenerarTokenValido() {
        Usuario usuario = Usuario.builder()
                .username("testuser")
                .role(Role.USER)
                .build();

        String token = jwtService.generateToken(usuario);

        assertNotNull(token);
        assertEquals("testuser", jwtService.extractUsername(token));
    }

    @Test
    void isTokenValid_deberiaRetornarTrue_cuandoTokenEsValido() {
        Usuario usuario = Usuario.builder()
                .username("testuser")
                .role(Role.USER)
                .build();

        String token = jwtService.generateToken(usuario);

        assertTrue(jwtService.isTokenValid(token, usuario));
    }

    @Test
    void isTokenValid_deberiaRetornarFalse_cuandoUsernameNoCoincide() {
        Usuario usuario = Usuario.builder()
                .username("testuser")
                .role(Role.USER)
                .build();

        String token = jwtService.generateToken(usuario);

        Usuario otroUsuario = Usuario.builder()
                .username("otro")
                .role(Role.USER)
                .build();

        assertFalse(jwtService.isTokenValid(token, otroUsuario));
    }
}
