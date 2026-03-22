package com.parqueadero.dto;

import com.parqueadero.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        AuthResponse response = new AuthResponse();

        // Assert
        assertNotNull(response);
        assertNull(response.getToken());
        assertNull(response.getRole());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        AuthResponse response = AuthResponse.builder()
                .token("jwt-token-12345")
                .role(Role.ADMIN)
                .build();

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token-12345", response.getToken());
        assertEquals(Role.ADMIN, response.getRole());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        AuthResponse response = new AuthResponse("jwt-token-67890", Role.OPERADOR);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token-67890", response.getToken());
        assertEquals(Role.OPERADOR, response.getRole());
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosTokens() {
        // Arrange
        AuthResponse response1 = AuthResponse.builder().token("token-123").build();
        AuthResponse response2 = AuthResponse.builder().token("token-123").build();

        // Act & Assert
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraTokensDiferentes() {
        // Arrange
        AuthResponse response1 = AuthResponse.builder().token("token-123").build();
        AuthResponse response2 = AuthResponse.builder().token("token-456").build();

        // Act & Assert
        assertNotEquals(response1, response2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        AuthResponse response = AuthResponse.builder()
                .token("jwt-token-abc")
                .role(Role.ADMIN)
                .build();

        // Act
        String resultado = response.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("jwt-token-abc"));
        assertTrue(resultado.contains("ADMIN"));
    }

    @Test
    void builder_deberiaSoportarTodosLosRoles() {
        // Arrange & Act & Assert
        AuthResponse admin = AuthResponse.builder().role(Role.ADMIN).build();
        AuthResponse operador = AuthResponse.builder().role(Role.OPERADOR).build();
        AuthResponse user = AuthResponse.builder().role(Role.USER).build();

        assertEquals(Role.ADMIN, admin.getRole());
        assertEquals(Role.OPERADOR, operador.getRole());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void token_deberiaAceptarDiferentesFormatos() {
        // Arrange & Act
        AuthResponse response1 = AuthResponse.builder().token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...").build();
        AuthResponse response2 = AuthResponse.builder().token("Bearer token123").build();
        AuthResponse response3 = AuthResponse.builder().token("simple-token").build();

        // Assert
        assertTrue(response1.getToken().startsWith("eyJ"));
        assertTrue(response2.getToken().startsWith("Bearer"));
        assertEquals("simple-token", response3.getToken());
    }

    @Test
    void builder_deberiaPermitirConstruirParcialmente() {
        // Arrange & Act
        AuthResponse response = AuthResponse.builder()
                .token("partial-token")
                .build();

        // Assert
        assertEquals("partial-token", response.getToken());
        assertNull(response.getRole());
    }
}
