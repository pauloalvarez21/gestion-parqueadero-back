package com.parqueadero.entity;

import com.parqueadero.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        Usuario usuario = new Usuario();

        // Assert
        assertNotNull(usuario);
        assertNull(usuario.getId());
        assertNull(usuario.getUsername());
        assertNull(usuario.getPassword());
        assertNull(usuario.getRole());
        assertTrue(usuario.isActivo());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        Usuario usuario = Usuario.builder()
                .id(1L)
                .username("admin")
                .password("password123")
                .role(Role.ADMIN)
                .activo(true)
                .build();

        // Assert
        assertNotNull(usuario);
        assertEquals(1L, usuario.getId());
        assertEquals("admin", usuario.getUsername());
        assertEquals("password123", usuario.getPassword());
        assertEquals(Role.ADMIN, usuario.getRole());
        assertTrue(usuario.isActivo());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        Usuario usuario = new Usuario(
                1L,
                "operador",
                "pass456",
                Role.OPERADOR,
                true
        );

        // Assert
        assertNotNull(usuario);
        assertEquals("operador", usuario.getUsername());
        assertEquals(Role.OPERADOR, usuario.getRole());
        assertTrue(usuario.isActivo());
    }

    @Test
    void getAuthorities_deberiaRetornarRolComoGrantedAuthority() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .username("admin")
                .password("pass")
                .role(Role.ADMIN)
                .build();

        // Act
        var authorities = usuario.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ADMIN")));
    }

    @Test
    void getAuthorities_deberiaRetornarRolOperador() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .role(Role.OPERADOR)
                .build();

        // Act
        var authorities = usuario.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("OPERADOR")));
    }

    @Test
    void isAccountNonExpired_deberiaRetornarTrue() {
        // Arrange
        Usuario usuario = Usuario.builder().build();

        // Act & Assert
        assertTrue(usuario.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_deberiaRetornarTrue() {
        // Arrange
        Usuario usuario = Usuario.builder().build();

        // Act & Assert
        assertTrue(usuario.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_deberiaRetornarTrue() {
        // Arrange
        Usuario usuario = Usuario.builder().build();

        // Act & Assert
        assertTrue(usuario.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_deberiaRetornarTrue_cuandoActivoEsTrue() {
        // Arrange
        Usuario usuario = Usuario.builder().activo(true).build();

        // Act & Assert
        assertTrue(usuario.isEnabled());
    }

    @Test
    void isEnabled_deberiaRetornarFalse_cuandoActivoEsFalse() {
        // Arrange
        Usuario usuario = Usuario.builder().activo(false).build();

        // Act & Assert
        assertFalse(usuario.isEnabled());
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosIds() {
        // Arrange
        Usuario usuario1 = Usuario.builder().id(1L).build();
        Usuario usuario2 = Usuario.builder().id(1L).build();

        // Act & Assert
        assertEquals(usuario1, usuario2);
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraIdsDiferentes() {
        // Arrange
        Usuario usuario1 = Usuario.builder().id(1L).build();
        Usuario usuario2 = Usuario.builder().id(2L).build();

        // Act & Assert
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .id(1L)
                .username("testuser")
                .role(Role.USER)
                .activo(true)
                .build();

        // Act
        String resultado = usuario.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("testuser"));
        assertTrue(resultado.contains("USER"));
    }

    @Test
    void builder_deberiaSoportarTodosLosRoles() {
        // Arrange & Act & Assert
        Usuario admin = Usuario.builder().role(Role.ADMIN).build();
        Usuario operador = Usuario.builder().role(Role.OPERADOR).build();
        Usuario user = Usuario.builder().role(Role.USER).build();

        assertEquals(Role.ADMIN, admin.getRole());
        assertEquals(Role.OPERADOR, operador.getRole());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void password_deberiaAceptarValoresComplejos() {
        // Arrange
        String passwordCompleja = "P@ssw0rd!2024#Secure";

        // Act
        Usuario usuario = Usuario.builder().password(passwordCompleja).build();

        // Assert
        assertNotNull(usuario.getPassword());
        assertEquals(passwordCompleja, usuario.getPassword());
    }

    @Test
    void username_deberiaAceptarDiferentesFormatos() {
        // Arrange & Act
        Usuario usuario1 = Usuario.builder().username("admin").build();
        Usuario usuario2 = Usuario.builder().username("user@test.com").build();
        Usuario usuario3 = Usuario.builder().username("operador_01").build();

        // Assert
        assertEquals("admin", usuario1.getUsername());
        assertEquals("user@test.com", usuario2.getUsername());
        assertEquals("operador_01", usuario3.getUsername());
    }

    @Test
    void activo_deberiaTenerValorPorDefectoTrue() {
        // Arrange & Act
        Usuario usuario = Usuario.builder()
                .username("test")
                .password("pass")
                .build();

        // Assert
        assertTrue(usuario.isActivo());
        assertTrue(usuario.isEnabled());
    }

    @Test
    void implementsUserDetails_deberiaTenerTodosLosMetodos() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .username("test")
                .password("pass")
                .role(Role.USER)
                .activo(true)
                .build();

        // Act & Assert
        assertNotNull(usuario.getUsername());
        assertNotNull(usuario.getPassword());
        assertNotNull(usuario.getAuthorities());
        assertTrue(usuario.isAccountNonExpired());
        assertTrue(usuario.isAccountNonLocked());
        assertTrue(usuario.isCredentialsNonExpired());
        assertTrue(usuario.isEnabled());
    }
}
