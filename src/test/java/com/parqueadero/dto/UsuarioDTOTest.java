package com.parqueadero.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDTOTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        UsuarioDTO dto = new UsuarioDTO();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getUsername());
        assertNull(dto.getRole());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        UsuarioDTO dto = UsuarioDTO.builder()
                .id(1L)
                .username("admin")
                .role("ADMIN")
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("admin", dto.getUsername());
        assertEquals("ADMIN", dto.getRole());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        UsuarioDTO dto = new UsuarioDTO(1L, "operador", "OPERADOR");

        // Assert
        assertNotNull(dto);
        assertEquals("operador", dto.getUsername());
        assertEquals("OPERADOR", dto.getRole());
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosIds() {
        // Arrange
        UsuarioDTO dto1 = UsuarioDTO.builder().id(1L).build();
        UsuarioDTO dto2 = UsuarioDTO.builder().id(1L).build();

        // Act & Assert
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraIdsDiferentes() {
        // Arrange
        UsuarioDTO dto1 = UsuarioDTO.builder().id(1L).build();
        UsuarioDTO dto2 = UsuarioDTO.builder().id(2L).build();

        // Act & Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        UsuarioDTO dto = UsuarioDTO.builder()
                .id(1L)
                .username("testuser")
                .role("USER")
                .build();

        // Act
        String resultado = dto.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("testuser"));
        assertTrue(resultado.contains("USER"));
    }

    @Test
    void roles_deberiaAceptarTodosLosValores() {
        // Arrange & Act
        UsuarioDTO admin = UsuarioDTO.builder().role("ADMIN").build();
        UsuarioDTO operador = UsuarioDTO.builder().role("OPERADOR").build();
        UsuarioDTO user = UsuarioDTO.builder().role("USER").build();

        // Assert
        assertEquals("ADMIN", admin.getRole());
        assertEquals("OPERADOR", operador.getRole());
        assertEquals("USER", user.getRole());
    }

    @Test
    void username_deberiaAceptarDiferentesFormatos() {
        // Arrange & Act
        UsuarioDTO dto1 = UsuarioDTO.builder().username("admin").build();
        UsuarioDTO dto2 = UsuarioDTO.builder().username("user@test.com").build();
        UsuarioDTO dto3 = UsuarioDTO.builder().username("operador_01").build();

        // Assert
        assertEquals("admin", dto1.getUsername());
        assertEquals("user@test.com", dto2.getUsername());
        assertEquals("operador_01", dto3.getUsername());
    }

    @Test
    void builder_deberiaPermitirConstruirParcialmente() {
        // Arrange & Act
        UsuarioDTO dto = UsuarioDTO.builder()
                .username("parcial")
                .build();

        // Assert
        assertEquals("parcial", dto.getUsername());
        assertNull(dto.getId());
        assertNull(dto.getRole());
    }
}
