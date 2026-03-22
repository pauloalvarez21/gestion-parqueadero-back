package com.parqueadero.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        LoginRequest request = new LoginRequest();

        // Assert
        assertNotNull(request);
        assertNull(request.getUsername());
        assertNull(request.getPassword());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        LoginRequest request = LoginRequest.builder()
                .username("admin")
                .password("password123")
                .build();

        // Assert
        assertNotNull(request);
        assertEquals("admin", request.getUsername());
        assertEquals("password123", request.getPassword());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        LoginRequest request = new LoginRequest("operador", "pass456");

        // Assert
        assertNotNull(request);
        assertEquals("operador", request.getUsername());
        assertEquals("pass456", request.getPassword());
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosValores() {
        // Arrange
        LoginRequest request1 = LoginRequest.builder().username("admin").build();
        LoginRequest request2 = LoginRequest.builder().username("admin").build();

        // Act & Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraUsernamesDiferentes() {
        // Arrange
        LoginRequest request1 = LoginRequest.builder().username("admin").build();
        LoginRequest request2 = LoginRequest.builder().username("operador").build();

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        LoginRequest request = LoginRequest.builder()
                .username("testuser")
                .password("secret")
                .build();

        // Act
        String resultado = request.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("testuser"));
    }

    @Test
    void username_deberiaAceptarDiferentesFormatos() {
        // Arrange & Act
        LoginRequest request1 = LoginRequest.builder().username("admin").build();
        LoginRequest request2 = LoginRequest.builder().username("user@test.com").build();
        LoginRequest request3 = LoginRequest.builder().username("operador_01").build();

        // Assert
        assertEquals("admin", request1.getUsername());
        assertEquals("user@test.com", request2.getUsername());
        assertEquals("operador_01", request3.getUsername());
    }

    @Test
    void password_deberiaAceptarValoresComplejos() {
        // Arrange
        String passwordCompleja = "P@ssw0rd!2024#Secure";

        // Act
        LoginRequest request = LoginRequest.builder().password(passwordCompleja).build();

        // Assert
        assertNotNull(request.getPassword());
        assertEquals(passwordCompleja, request.getPassword());
    }

    @Test
    void builder_deberiaPermitirConstruirParcialmente() {
        // Arrange & Act
        LoginRequest request = LoginRequest.builder()
                .username("parcial")
                .build();

        // Assert
        assertEquals("parcial", request.getUsername());
        assertNull(request.getPassword());
    }

    @Test
    void campos_deberianAceptarCadenasVacias() {
        // Arrange & Act
        LoginRequest request = LoginRequest.builder()
                .username("")
                .password("")
                .build();

        // Assert
        assertEquals("", request.getUsername());
        assertEquals("", request.getPassword());
    }
}
